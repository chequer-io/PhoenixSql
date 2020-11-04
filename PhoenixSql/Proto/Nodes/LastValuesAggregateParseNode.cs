using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class LastValuesAggregateParseNode : IDelegateConstantToCountParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
