using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class IsNullParseNode : IUnaryParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

