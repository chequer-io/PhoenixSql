using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class SequenceValueParseNode : ITerminalParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

