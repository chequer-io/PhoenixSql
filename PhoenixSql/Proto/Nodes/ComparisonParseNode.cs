using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class ComparisonParseNode : IBinaryParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

