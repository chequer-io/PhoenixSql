using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class LastValueAggregateParseNode : IDelegateConstantToCountParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
