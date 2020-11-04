using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class RoundParseNode : IFunctionParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
