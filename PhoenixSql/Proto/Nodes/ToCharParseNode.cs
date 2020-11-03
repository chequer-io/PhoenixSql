using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class ToCharParseNode : IFunctionParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

