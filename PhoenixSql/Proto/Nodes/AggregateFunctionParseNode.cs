using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class AggregateFunctionParseNode : IFunctionParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

