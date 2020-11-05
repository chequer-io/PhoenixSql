namespace PhoenixSql
{
    public partial class W_FunctionParseNode : IProxyMessage<IFunctionParseNode>, IFunctionParseNode
    {
        public string Alias => Message.Alias;

        public System.Collections.Generic.IReadOnlyList<IParseNode> Children => Message.Children;

        public bool IsStateless => Message.IsStateless;

        public string Name => Message.Name;

        public bool IsAggregate => Message.IsAggregate;

        public IFunctionParseNode Message => (IFunctionParseNode)inherit_;
    }
}