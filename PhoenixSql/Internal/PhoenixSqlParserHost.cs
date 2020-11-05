using System;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Runtime.ExceptionServices;
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
        private const string hostLibrary = "lib/PhoenixSql.Host-1.0-SNAPSHOT-jar-with-dependencies.jar";

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

        public void Initialize()
        {
            _event.WaitOne();
        }

        private void Reconnect()
        {
            Shutdown();
            Connect();
        }

        private void Connect()
        {
            VerifyStatus(HostStatus.WatingToConnect);
            VerifyHostLibrary();
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
            Reconnect();
        }

        private void SpawnHostProcess(in int boundPort)
        {
            VerifyStatus(HostStatus.Handshaking);

            _hostProcess = new Process
            {
                StartInfo = new ProcessStartInfo
                {
                    FileName = PathUtility.GetRuntimePath(),
                    Arguments = $"-jar {hostLibrary} {boundPort}",
                    UseShellExecute = false,
                    RedirectStandardOutput = true,
                    RedirectStandardError = true
                },
                EnableRaisingEvents = true
            };

            _hostProcess.Exited += HostProcessOnExited;
            _hostProcess.Start();
        }

        private void HostProcessOnExited(object sender, EventArgs e)
        {
            Reconnect();
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
            _status = HostStatus.WatingToConnect;
        }

        private void VerifyStatus(HostStatus status)
        {
            if (status != _status)
                throw new PhoenixSqlHostException($"Host status must be '{status}', but was '{_status}'.");
        }

        private void VerifyHostLibrary()
        {
            if (!File.Exists(hostLibrary))
                throw new PhoenixSqlHostException($"{hostLibrary} not found.");
        }

        private void VerifyJavaRuntime()
        {
            var runtimePath = PathUtility.GetRuntimePath();

            if (!Directory.Exists(Path.GetDirectoryName(runtimePath)))
                throw new RuntimeNotFoundException();
        }

        public IBindableStatement Parse(string sql)
        {
            try
            {
                _event.WaitOne();
                VerifyStatus(HostStatus.Connected);
                return _hostService.parse(new ParseRequest { Sql = sql }).Message;
            }
            catch (RpcException e)
            {
                throw HandleParseError(e);
            }
        }

        public async Task<IBindableStatement> ParseAsync(string sql)
        {
            try
            {
                _event.WaitOne();
                VerifyStatus(HostStatus.Connected);
                return (await _hostService.parseAsync(new ParseRequest { Sql = sql })).Message;
            }
            catch (RpcException e)
            {
                throw HandleParseError(e);
            }
        }

        private static PhoenixSqlException HandleParseError(RpcException e)
        {
            if (e.Status.StatusCode == StatusCode.Aborted)
                return new PhoenixSqlSyntaxException(e.Status.Detail);

            return new PhoenixSqlHostException(e.Status.Detail);
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
