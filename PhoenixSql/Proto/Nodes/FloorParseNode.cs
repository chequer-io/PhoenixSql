using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class FloorParseNode : IFunctionParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
