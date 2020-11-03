using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class DistinctCountParseNode : IDelegateConstantToCountParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

