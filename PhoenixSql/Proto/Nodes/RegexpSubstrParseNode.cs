using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class RegexpSubstrParseNode : IFunctionParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
