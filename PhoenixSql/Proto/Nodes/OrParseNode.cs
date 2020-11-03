using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class OrParseNode : ICompoundParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

