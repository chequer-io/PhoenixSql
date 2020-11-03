using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class CurrentTimeParseNode : IFunctionParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

