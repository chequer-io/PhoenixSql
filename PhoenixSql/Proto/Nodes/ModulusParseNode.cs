using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class ModulusParseNode : IArithmeticParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
