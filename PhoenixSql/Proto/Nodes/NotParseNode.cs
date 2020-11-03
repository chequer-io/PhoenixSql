using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class NotParseNode : IUnaryParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

