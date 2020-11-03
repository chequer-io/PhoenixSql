using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class ColumnParseNode : INamedParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

