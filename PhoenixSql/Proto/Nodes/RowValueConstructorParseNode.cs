using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class RowValueConstructorParseNode : ICompoundParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

