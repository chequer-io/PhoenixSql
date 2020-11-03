using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class ExistsParseNode : IUnaryParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

