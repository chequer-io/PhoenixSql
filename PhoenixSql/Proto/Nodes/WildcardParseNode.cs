using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class WildcardParseNode : ITerminalParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

