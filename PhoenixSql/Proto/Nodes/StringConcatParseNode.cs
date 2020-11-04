using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class StringConcatParseNode : ICompoundParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
