using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class ArrayAnyComparisonNode : IArrayAllAnyComparisonNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
