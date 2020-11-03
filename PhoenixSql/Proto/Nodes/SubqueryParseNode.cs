using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class SubqueryParseNode : ITerminalParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

