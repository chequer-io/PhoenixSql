using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class InParseNode : IBinaryParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

