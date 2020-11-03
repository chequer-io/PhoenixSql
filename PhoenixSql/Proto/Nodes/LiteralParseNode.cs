using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class LiteralParseNode : ITerminalParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

