using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class AndParseNode : ICompoundParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
