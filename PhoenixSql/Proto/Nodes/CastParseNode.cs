using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class CastParseNode : IUnaryParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

