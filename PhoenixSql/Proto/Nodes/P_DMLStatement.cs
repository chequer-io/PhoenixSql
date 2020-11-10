namespace PhoenixSql
{
    public partial class P_DMLStatement : IPhoenixProxyNode<IDMLStatement>, IDMLStatement
    {
        public int BindCount => Message.BindCount;

        public Operation Operation => Message.Operation;

        public NamedTableNode Table => Message.Table;

        public IDMLStatement Message => (IDMLStatement)inherit_;
    }
}