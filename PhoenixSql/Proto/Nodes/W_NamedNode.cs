namespace PhoenixSql
{
    public partial class W_NamedNode : INamedNode
    {
        public string Name => Value.Name;

        public bool IsCaseSensitive => Value.IsCaseSensitive;

        public INamedNode Value => (INamedNode)inherit_;
    }
}
