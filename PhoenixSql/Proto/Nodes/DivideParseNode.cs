using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class DivideParseNode : IArithmeticParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
