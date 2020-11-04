using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class RegexpReplaceParseNode : IFunctionParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
