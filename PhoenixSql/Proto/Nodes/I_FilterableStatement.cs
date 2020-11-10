namespace PhoenixSql
{
    public partial class I_FilterableStatement : IPhoenixProxyNode<IFilterableStatement>, IFilterableStatement
    {
        public int BindCount => Message.BindCount;

        public Operation Operation => Message.Operation;

        public HintNode Hint => Message.Hint;

        public LimitNode Limit => Message.Limit;

        public OffsetNode Offset => Message.Offset;

        public Google.Protobuf.Collections.RepeatedField<OrderByNode> OrderBy => Message.OrderBy;

        public double TableSamplingRate => Message.TableSamplingRate;

        public IParseNode Where => Message.Where;

        public bool IsAggregate => Message.IsAggregate;

        public bool IsDistinct => Message.IsDistinct;

        public IFilterableStatement Message => (IFilterableStatement)inherit_;
    }
}