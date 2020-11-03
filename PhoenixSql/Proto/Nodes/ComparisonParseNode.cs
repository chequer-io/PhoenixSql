using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class ComparisonParseNode : IBinaryParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

