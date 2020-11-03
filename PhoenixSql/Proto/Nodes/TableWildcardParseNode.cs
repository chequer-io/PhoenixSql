using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class TableWildcardParseNode : INamedParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

