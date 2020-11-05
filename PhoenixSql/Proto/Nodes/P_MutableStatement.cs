namespace PhoenixSql
{
    public partial class P_MutableStatement : IProxyMessage<IMutableStatement>, IMutableStatement
    {
        public int BindCount => Message.BindCount;

        public Operation Operation => Message.Operation;

        public IMutableStatement Message => (IMutableStatement)inherit_;
    }
}