namespace PhoenixSql
{
    public partial class W_NamedNode : IProxyMessage<INamedNode>, INamedNode
    {
        public string Name => Message.Name;

        public bool IsCaseSensitive => Message.IsCaseSensitive;

        public INamedNode Message => (INamedNode)inherit_;
    }
}