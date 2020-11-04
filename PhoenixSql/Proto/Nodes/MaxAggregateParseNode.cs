using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class MaxAggregateParseNode : IDelegateConstantToCountParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
