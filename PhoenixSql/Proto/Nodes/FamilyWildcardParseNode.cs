using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class FamilyWildcardParseNode : INamedParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

