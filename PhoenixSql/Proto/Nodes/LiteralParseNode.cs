using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class LiteralParseNode : ITerminalParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

