using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class LessThanParseNode : IComparisonParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
