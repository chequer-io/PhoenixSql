using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class LikeParseNode : IBinaryParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

