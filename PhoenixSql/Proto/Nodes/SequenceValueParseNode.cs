using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class SequenceValueParseNode : ITerminalParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

