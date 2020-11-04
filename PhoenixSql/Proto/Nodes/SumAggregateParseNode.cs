using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class SumAggregateParseNode : IDelegateConstantToCountParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
