using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class NthValueAggregateParseNode : IDelegateConstantToCountParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

