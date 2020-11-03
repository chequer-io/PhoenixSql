using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class BetweenParseNode : ICompoundParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

