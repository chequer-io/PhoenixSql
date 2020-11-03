using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class RowValueConstructorParseNode : ICompoundParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

