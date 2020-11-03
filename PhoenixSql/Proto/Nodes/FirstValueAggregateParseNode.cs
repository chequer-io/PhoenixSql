using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class FirstValueAggregateParseNode : IDelegateConstantToCountParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

