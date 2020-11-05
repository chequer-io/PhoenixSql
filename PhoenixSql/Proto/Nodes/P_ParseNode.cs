namespace PhoenixSql
{
    public partial class P_ParseNode : IProxyMessage<IParseNode>, IParseNode
    {
        public string Alias => Message.Alias;

        public System.Collections.Generic.IReadOnlyList<IParseNode> Children => Message.Children;

        public bool IsStateless => Message.IsStateless;

        public IParseNode Message => (IParseNode)inherit_;
    }
}