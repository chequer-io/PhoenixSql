using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class MinAggregateParseNode : IDelegateConstantToCountParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

