using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class ArithmeticParseNode : ICompoundParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

