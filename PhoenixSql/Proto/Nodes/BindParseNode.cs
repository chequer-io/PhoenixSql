using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public partial class BindParseNode : INamedParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => children_;
    }
}

