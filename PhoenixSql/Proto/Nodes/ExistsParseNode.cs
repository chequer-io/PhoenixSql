using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class ExistsParseNode : IUnaryParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
