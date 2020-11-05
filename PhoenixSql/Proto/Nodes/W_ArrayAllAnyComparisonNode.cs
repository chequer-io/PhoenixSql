namespace PhoenixSql
{
    public partial class W_ArrayAllAnyComparisonNode : IProxyMessage<IArrayAllAnyComparisonNode>, IArrayAllAnyComparisonNode
    {
        public string Alias => Message.Alias;

        public System.Collections.Generic.IReadOnlyList<IParseNode> Children => Message.Children;

        public bool IsStateless => Message.IsStateless;

        public string Type => Message.Type;

        public IArrayAllAnyComparisonNode Message => (IArrayAllAnyComparisonNode)inherit_;
    }
}