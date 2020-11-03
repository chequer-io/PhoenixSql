using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class ToTimestampParseNode : IFunctionParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

