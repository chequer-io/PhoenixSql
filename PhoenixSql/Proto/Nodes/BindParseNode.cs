using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class BindParseNode : INamedParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}
