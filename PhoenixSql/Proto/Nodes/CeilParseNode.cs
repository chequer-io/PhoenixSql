using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class CeilParseNode : IFunctionParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
