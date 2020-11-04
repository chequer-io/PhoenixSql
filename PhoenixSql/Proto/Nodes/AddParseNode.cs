using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class AddParseNode : IArithmeticParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
