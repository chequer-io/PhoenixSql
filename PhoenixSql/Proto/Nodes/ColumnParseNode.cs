using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class ColumnParseNode : INamedParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

