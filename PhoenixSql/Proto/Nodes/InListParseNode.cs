using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class InListParseNode : ICompoundParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

