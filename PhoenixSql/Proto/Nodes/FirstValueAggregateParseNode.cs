using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class FirstValueAggregateParseNode : IDelegateConstantToCountParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
