using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class SumAggregateParseNode : IDelegateConstantToCountParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

