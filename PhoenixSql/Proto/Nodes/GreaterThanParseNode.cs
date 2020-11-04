using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class GreaterThanParseNode : IComparisonParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
