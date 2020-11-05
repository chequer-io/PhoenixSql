namespace PhoenixSql
{
    public partial class W_NamedParseNode : IProxyMessage<INamedParseNode>, INamedParseNode
    {
        public string Alias => Message.Alias;

        public System.Collections.Generic.IReadOnlyList<IParseNode> Children => Message.Children;

        public bool IsStateless => Message.IsStateless;

        public string Name => Message.Name;

        public bool IsCaseSensitive => Message.IsCaseSensitive;

        public INamedParseNode Message => (INamedParseNode)inherit_;
    }
}