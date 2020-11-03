using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class FloorParseNode : IFunctionParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

