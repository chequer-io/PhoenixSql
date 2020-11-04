using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class FirstValuesAggregateParseNode : IDelegateConstantToCountParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
