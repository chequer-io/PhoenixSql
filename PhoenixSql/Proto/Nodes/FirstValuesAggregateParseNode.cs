using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class FirstValuesAggregateParseNode : IDelegateConstantToCountParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

