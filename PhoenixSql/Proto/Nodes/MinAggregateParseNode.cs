using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class MinAggregateParseNode : IDelegateConstantToCountParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
