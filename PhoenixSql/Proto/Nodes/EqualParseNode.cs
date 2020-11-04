using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class EqualParseNode : IComparisonParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
