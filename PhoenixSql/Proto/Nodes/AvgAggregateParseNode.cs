using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class AvgAggregateParseNode : IAggregateFunctionParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

