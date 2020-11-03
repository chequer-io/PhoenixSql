using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class ArrayModifierParseNode : IFunctionParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

