namespace PhoenixSql
{
    public partial class P_NamedNode : IPhoenixProxyNode<INamedNode>, INamedNode
    {
        public string Name => Message.Name;

        public bool IsCaseSensitive => Message.IsCaseSensitive;

        public INamedNode Message => (INamedNode)inherit_;
    }
}