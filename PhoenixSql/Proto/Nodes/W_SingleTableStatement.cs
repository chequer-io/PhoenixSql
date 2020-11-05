namespace PhoenixSql
{
    public partial class W_SingleTableStatement : IProxyMessage<ISingleTableStatement>, ISingleTableStatement
    {
        public int BindCount => Message.BindCount;

        public Operation Operation => Message.Operation;

        public NamedTableNode Table => Message.Table;

        public ISingleTableStatement Message => (ISingleTableStatement)inherit_;
    }
}