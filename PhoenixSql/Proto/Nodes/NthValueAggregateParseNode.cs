using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class NthValueAggregateParseNode : IDelegateConstantToCountParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
