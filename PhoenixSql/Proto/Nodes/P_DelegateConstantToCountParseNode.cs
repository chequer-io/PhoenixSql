namespace PhoenixSql
{
    public partial class P_DelegateConstantToCountParseNode : IProxyMessage<IDelegateConstantToCountParseNode>, IDelegateConstantToCountParseNode
    {
        public string Alias => Message.Alias;

        public System.Collections.Generic.IReadOnlyList<IParseNode> Children => Message.Children;

        public bool IsStateless => Message.IsStateless;

        public string Name => Message.Name;

        public bool IsAggregate => Message.IsAggregate;

        public IDelegateConstantToCountParseNode Message => (IDelegateConstantToCountParseNode)inherit_;
    }
}