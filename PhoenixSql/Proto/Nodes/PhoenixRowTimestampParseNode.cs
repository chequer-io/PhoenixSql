using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class PhoenixRowTimestampParseNode : IFunctionParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

