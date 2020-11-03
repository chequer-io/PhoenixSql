using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class ToDateParseNode : IFunctionParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

