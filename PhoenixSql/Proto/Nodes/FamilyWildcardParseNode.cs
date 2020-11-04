using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class FamilyWildcardParseNode : INamedParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
