using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class GreaterThanOrEqualParseNode : IComparisonParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
