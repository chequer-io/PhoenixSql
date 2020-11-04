using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class UDFParseNode : IFunctionParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
