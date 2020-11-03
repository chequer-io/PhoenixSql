using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class StringConcatParseNode : ICompoundParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

