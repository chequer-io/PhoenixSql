using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class AggregateFunctionWithinGroupParseNode : IAggregateFunctionParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

