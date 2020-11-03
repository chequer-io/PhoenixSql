using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class LastValuesAggregateParseNode : IDelegateConstantToCountParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

