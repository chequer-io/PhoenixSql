using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class ToCharParseNode : IFunctionParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
