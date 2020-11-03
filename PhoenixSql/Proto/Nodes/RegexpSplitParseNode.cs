using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class RegexpSplitParseNode : IFunctionParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

