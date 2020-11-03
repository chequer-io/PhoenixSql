using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class AndParseNode : ICompoundParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

