namespace PhoenixSql
{
    public interface IFilterableStatement : IBindableStatement
    {
        HintNode Hint { get; }

        LimitNode Limit { get; }

        OffsetNode Offset { get; }

        Google.Protobuf.Collections.RepeatedField<OrderByNode> OrderBy { get; }

        double TableSamplingRate { get; }

        IParseNode Where { get; }

        bool IsAggregate { get; }

        bool IsDistinct { get; }
    }
}
