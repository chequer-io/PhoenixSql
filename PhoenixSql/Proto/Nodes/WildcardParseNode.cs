using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class WildcardParseNode : ITerminalParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

