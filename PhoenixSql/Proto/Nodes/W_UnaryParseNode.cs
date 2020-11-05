namespace PhoenixSql
{
    public partial class W_UnaryParseNode : IProxyMessage<IUnaryParseNode>, IUnaryParseNode
    {
        public string Alias => Message.Alias;

        public System.Collections.Generic.IReadOnlyList<IParseNode> Children => Message.Children;

        public bool IsStateless => Message.IsStateless;

        public IUnaryParseNode Message => (IUnaryParseNode)inherit_;
    }
}