using System.Threading.Tasks;
using Grpc.Core;
using PhoenixSql.Internal;

namespace PhoenixSql.Services
{
    internal class HandshakeRpc : HandshakeService.HandshakeServiceBase
    {
        public delegate Task<AckResponse> AckDelegate(AckRequest request);

        private readonly AckDelegate _ackDelegate;

        internal HandshakeRpc(AckDelegate ackDelegate)
        {
            _ackDelegate = ackDelegate;
        }

        public override async Task<AckResponse> ack(AckRequest request, ServerCallContext context)
        {
            return await _ackDelegate(request);
        }
    }
}
