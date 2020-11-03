using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class CaseParseNode : ICompoundParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

