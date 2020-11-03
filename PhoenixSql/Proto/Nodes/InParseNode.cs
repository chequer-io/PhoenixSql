using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class InParseNode : IBinaryParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

