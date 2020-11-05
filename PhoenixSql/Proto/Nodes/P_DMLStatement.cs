namespace PhoenixSql
{
    public partial class P_DMLStatement : IProxyMessage<IDMLStatement>, IDMLStatement
    {
        public int BindCount => Message.BindCount;

        public Operation Operation => Message.Operation;

        public NamedTableNode Table => Message.Table;

        public Google.Protobuf.Collections.RepeatedField<UDFMapEntry> UdfParseNodes => Message.UdfParseNodes;

        public IDMLStatement Message => (IDMLStatement)inherit_;
    }
}