using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class ToTimestampParseNode : IFunctionParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
