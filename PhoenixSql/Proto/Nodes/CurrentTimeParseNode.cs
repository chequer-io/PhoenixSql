using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class CurrentTimeParseNode : IFunctionParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

