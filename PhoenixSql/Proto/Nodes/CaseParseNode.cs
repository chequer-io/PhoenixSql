using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class CaseParseNode : ICompoundParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
