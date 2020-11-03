using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class CurrentDateParseNode : IFunctionParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

