using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class SubtractParseNode : IArithmeticParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
