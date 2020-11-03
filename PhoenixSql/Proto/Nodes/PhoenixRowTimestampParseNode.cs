using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class PhoenixRowTimestampParseNode : IFunctionParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

