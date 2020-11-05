namespace PhoenixSql
{
    public partial class W_TerminalParseNode : IProxyMessage<ITerminalParseNode>, ITerminalParseNode
    {
        public string Alias => Message.Alias;

        public System.Collections.Generic.IReadOnlyList<IParseNode> Children => Message.Children;

        public bool IsStateless => Message.IsStateless;

        public ITerminalParseNode Message => (ITerminalParseNode)inherit_;
    }
}