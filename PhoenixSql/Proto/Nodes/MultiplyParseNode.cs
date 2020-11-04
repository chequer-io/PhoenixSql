using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class MultiplyParseNode : IArithmeticParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
