using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class RegexpReplaceParseNode : IFunctionParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

