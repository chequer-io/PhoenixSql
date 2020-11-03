using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class DistinctCountParseNode : IDelegateConstantToCountParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

