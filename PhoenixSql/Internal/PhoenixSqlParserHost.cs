﻿using System;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using Grpc.Core;
using J2NET.Exceptions;
using J2NET.Utilities;
using PhoenixSql.Services;

namespace PhoenixSql.Internal
{
    internal class PhoenixSqlParserHost
    {
        private const string jar = "lib/PhoenixSql.Host.jar";

        private Process _hostProcess;
        private Server _handshakeServer;

        private CancellationTokenSource _handshakeTimeoutTokenSource;
        private CancellationTokenRegistration _handshakeTimeoutTokenRegistration;

        private Channel _hostChannel;
        private HostService.HostServiceClient _hostService;

        private HostStatus _status = HostStatus.WatingToConnect;

        private readonly ManualResetEvent _event = new ManualResetEvent(false);

        private PhoenixSqlParserHost()
        {
            Connect();
        }

        private void Connect()
        {
            VerifyStatus(HostStatus.WatingToConnect);
            VerifyJavaRuntime();

            _status = HostStatus.WatingToHandshaking;
            _event.Reset();

            try
            {
                _handshakeServer = new Server
                {
                    Ports =
                    {
                        new ServerPort("localhost", 0, ServerCredentials.Insecure)
                    },
                    Services =
                    {
                        HandshakeService.BindService(new HandshakeRpc(HostProcessOnAck))
                    }
                };

                _handshakeServer.Start();

                _status = HostStatus.Handshaking;

                _handshakeTimeoutTokenSource = new CancellationTokenSource();
                _handshakeTimeoutTokenSource.CancelAfter(TimeSpan.FromSeconds(50));

                _handshakeTimeoutTokenRegistration = _handshakeTimeoutTokenSource.Token.Register(HandshakeTimeout);

                var boundPort = _handshakeServer.Ports.First().BoundPort;
                SpawnHostProcess(boundPort);
            }
            catch (Exception)
            {
                _handshakeServer?.KillAsync().Wait();
                _handshakeServer = null;

                throw new PhoenixSqlHostException("Host handshake failed.");
            }
        }

        private void HandshakeTimeout()
        {
            if (_status == HostStatus.Handshaking || _status == HostStatus.Connecting)
            {
                throw new PhoenixSqlHostException("Host handshake timeout.");
            }
        }

        private void SpawnHostProcess(in int boundPort)
        {
            VerifyStatus(HostStatus.Handshaking);

            _hostProcess = new Process
            {
                StartInfo = new ProcessStartInfo
                {
                    FileName = PathUtility.GetRuntimePath(),
                    Arguments = $"-jar {jar} {boundPort}"
                },
                EnableRaisingEvents = true
            };

            _hostProcess.Exited += HostProcessOnExited;
            _hostProcess.Start();
            Console.WriteLine(_hostProcess.Id);
        }

        private void HostProcessOnExited(object sender, EventArgs e)
        {
            bool reconnect = _status == HostStatus.Connected;

            Shutdown();

            if (reconnect)
            {
                _status = HostStatus.WatingToHandshaking;
                Connect();
            }
        }

        private async Task<AckResponse> HostProcessOnAck(AckRequest request)
        {
            VerifyStatus(HostStatus.Handshaking);

            _status = HostStatus.Connecting;

            try
            {
                _hostChannel = new Channel("localhost", request.Port, ChannelCredentials.Insecure);
                _hostService = new HostService.HostServiceClient(_hostChannel);

                await _hostService.pingAsync(new Empty());

                _status = HostStatus.Connected;

                await _handshakeTimeoutTokenRegistration.DisposeAsync();

                _event.Set();
            }
            catch (Exception)
            {
                if (_hostChannel != null)
                {
                    await _hostChannel.ShutdownAsync();
                    _hostChannel = null;
                }

                _hostService = null;

                throw new PhoenixSqlHostException("Host connecting failed.");
            }

            return new AckResponse();
        }

        private void Shutdown()
        {
            if (_handshakeTimeoutTokenSource != null)
            {
                _handshakeTimeoutTokenSource.Cancel();
                _handshakeTimeoutTokenSource.Dispose();
                _handshakeTimeoutTokenSource = null;
            }

            _handshakeTimeoutTokenRegistration.Dispose();
            _handshakeTimeoutTokenRegistration = default;

            if (_handshakeServer != null)
            {
                _handshakeServer.KillAsync().Wait();
                _handshakeServer = null;
            }

            if (_hostProcess != null)
            {
                _hostProcess.Exited -= HostProcessOnExited;
                _hostProcess.Kill();
                _hostProcess = null;
            }

            if (_hostChannel != null)
            {
                _hostChannel.ShutdownAsync().Wait();
                _hostChannel = null;
            }

            _hostService = null;
            _status = HostStatus.Closed;
        }

        private void VerifyStatus(HostStatus status)
        {
            if (status != _status)
                throw new PhoenixSqlHostException($"Host status must be '{status}', but was '{_status}'.");
        }

        private void VerifyJavaRuntime()
        {
            var runtimePath = PathUtility.GetRuntimePath();

            if (!Directory.Exists(Path.GetDirectoryName(runtimePath)))
                throw new RuntimeNotFoundException();
        }

        public IBindableStatement Parse(string sql)
        {
            _event.WaitOne();
            VerifyStatus(HostStatus.Connected);
            return _hostService.parse(new ParseRequest { Sql = sql }).Value;
        }

        public async Task<IBindableStatement> ParseAsync(string sql)
        {
            _event.WaitOne();
            VerifyStatus(HostStatus.Connected);
            return (await _hostService.parseAsync(new ParseRequest { Sql = sql })).Value;
        }

        #region Static
        private static readonly object _lock = new object();

        private static PhoenixSqlParserHost _instance;

        internal static PhoenixSqlParserHost Instance
        {
            get
            {
                lock (_lock)
                {
                    _instance ??= new PhoenixSqlParserHost();
                }

                return _instance;
            }
        }
        #endregion
    }
}