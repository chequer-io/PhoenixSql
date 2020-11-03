using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class SubqueryParseNode : ITerminalParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

