using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class DistinctCountHyperLogLogAggregateParseNode : IDelegateConstantToCountParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

