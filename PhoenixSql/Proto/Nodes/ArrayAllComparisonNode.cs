using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class ArrayAllComparisonNode : IArrayAllAnyComparisonNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
