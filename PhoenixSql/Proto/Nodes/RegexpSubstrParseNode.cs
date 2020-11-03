using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class RegexpSubstrParseNode : IFunctionParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

