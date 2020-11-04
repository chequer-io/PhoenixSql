using System.Collections.Generic;

namespace PhoenixSql
{
    public interface IFilterableStatement : IBindableStatement
    {
        OffsetNode Offset { get; }

        double TableSamplingRate { get; }

        HintNode Hint { get; }

        bool IsDistinct { get; }

        IParseNode Where { get; }

        LimitNode Limit { get; }

        bool IsAggregate { get; }

        IReadOnlyList<OrderByNode> OrderBy { get; }
    }
}
