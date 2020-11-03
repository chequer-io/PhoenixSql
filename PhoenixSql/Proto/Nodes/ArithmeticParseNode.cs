using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class ArithmeticParseNode : ICompoundParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

