using Google.Protobuf.Collections;

namespace PhoenixSql
{
    // apache/phoenix/parse/FilterableStatement.java
    public interface IFilterableStatement : IBindableStatement
    {
        HintNode Hint { get; }

        ParseNode Where { get; }

        bool IsDistinct { get; }

        bool IsAggregate { get; }

        RepeatedField<OrderByNode> OrderBy { get; }

        double TableSamplingRate { get; }

        LimitNode Limit { get; }

        OffsetNode Offset { get; }
    }
}
