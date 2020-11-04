using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class NotParseNode : IUnaryParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
