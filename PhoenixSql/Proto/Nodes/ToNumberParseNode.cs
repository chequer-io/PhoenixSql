using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class ToNumberParseNode : IFunctionParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
