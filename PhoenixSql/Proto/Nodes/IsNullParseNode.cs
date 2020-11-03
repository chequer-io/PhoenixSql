using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class IsNullParseNode : IUnaryParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

