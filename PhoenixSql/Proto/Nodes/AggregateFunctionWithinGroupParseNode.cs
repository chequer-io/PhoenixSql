using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class AggregateFunctionWithinGroupParseNode : IAggregateFunctionParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
