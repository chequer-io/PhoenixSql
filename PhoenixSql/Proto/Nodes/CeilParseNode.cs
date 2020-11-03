using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class CeilParseNode : IFunctionParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

