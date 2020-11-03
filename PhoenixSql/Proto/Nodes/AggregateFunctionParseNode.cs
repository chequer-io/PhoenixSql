using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class AggregateFunctionParseNode : IFunctionParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

