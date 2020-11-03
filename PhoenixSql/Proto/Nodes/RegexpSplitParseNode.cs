using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class RegexpSplitParseNode : IFunctionParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

