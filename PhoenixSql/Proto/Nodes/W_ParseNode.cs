using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class W_ParseNode : IParseNode
    {
        IReadOnlyList<IParseNode> IParseNode.Children => Value.Children;

        public bool IsStateless => Value.IsStateless;

        public string Alias => Value.Alias;

        public IParseNode Value => (IParseNode)inherit_;
    }
}
