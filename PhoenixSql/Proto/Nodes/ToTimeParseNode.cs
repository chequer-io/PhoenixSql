using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class ToTimeParseNode : IFunctionParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

