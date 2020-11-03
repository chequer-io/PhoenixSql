using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class LastValueAggregateParseNode : IDelegateConstantToCountParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

