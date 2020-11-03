using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class LikeParseNode : IBinaryParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

