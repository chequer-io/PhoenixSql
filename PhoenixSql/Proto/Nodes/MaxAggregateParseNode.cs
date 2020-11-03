using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class MaxAggregateParseNode : IDelegateConstantToCountParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

