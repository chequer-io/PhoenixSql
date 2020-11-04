using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class ArrayElemRefNode : ICompoundParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
