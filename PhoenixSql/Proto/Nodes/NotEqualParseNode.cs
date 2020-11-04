using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class NotEqualParseNode : IComparisonParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
