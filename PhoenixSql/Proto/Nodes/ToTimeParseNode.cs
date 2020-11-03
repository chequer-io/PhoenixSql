using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class ToTimeParseNode : IFunctionParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

