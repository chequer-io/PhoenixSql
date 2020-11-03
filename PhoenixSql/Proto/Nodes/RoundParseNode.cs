using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class RoundParseNode : IFunctionParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

