using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class OrParseNode : ICompoundParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

