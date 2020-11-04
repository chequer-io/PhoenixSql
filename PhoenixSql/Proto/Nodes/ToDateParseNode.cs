using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class ToDateParseNode : IFunctionParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
