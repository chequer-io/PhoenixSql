using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class ArrayConstructorNode : ICompoundParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
