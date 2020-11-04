using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class BetweenParseNode : ICompoundParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
