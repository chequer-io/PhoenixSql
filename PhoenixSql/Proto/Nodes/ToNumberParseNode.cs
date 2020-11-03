using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class ToNumberParseNode : IFunctionParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

