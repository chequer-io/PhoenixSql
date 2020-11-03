using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class DistinctCountHyperLogLogAggregateParseNode : IDelegateConstantToCountParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

