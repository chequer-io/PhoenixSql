using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class UDFParseNode : IFunctionParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

