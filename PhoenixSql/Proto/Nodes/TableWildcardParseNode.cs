using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class TableWildcardParseNode : INamedParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

