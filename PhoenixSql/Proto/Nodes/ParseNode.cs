using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class ParseNode : IParseNode
    {
        public bool IsStateless => Node.IsStateless;

        public IReadOnlyList<IParseNode> Children => Node.Children;

        public IParseNode Node => (IParseNode)inherit_;
    }
}
