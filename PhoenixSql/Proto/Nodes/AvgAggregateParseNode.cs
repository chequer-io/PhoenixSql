using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class AvgAggregateParseNode : IAggregateFunctionParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
