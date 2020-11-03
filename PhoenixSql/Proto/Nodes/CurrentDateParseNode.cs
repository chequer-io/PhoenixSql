using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class CurrentDateParseNode : IFunctionParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

