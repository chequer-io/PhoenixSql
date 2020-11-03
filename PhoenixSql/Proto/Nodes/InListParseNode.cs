using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class InListParseNode : ICompoundParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

