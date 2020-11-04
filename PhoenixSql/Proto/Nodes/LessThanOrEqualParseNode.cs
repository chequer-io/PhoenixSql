using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class LessThanOrEqualParseNode : IComparisonParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
