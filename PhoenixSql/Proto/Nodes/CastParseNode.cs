using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class CastParseNode : IUnaryParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
