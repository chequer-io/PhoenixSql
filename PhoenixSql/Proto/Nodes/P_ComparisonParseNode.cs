namespace PhoenixSql
{
    public partial class P_ComparisonParseNode : IProxyMessage<IComparisonParseNode>, IComparisonParseNode
    {
        public string Alias => Message.Alias;

        public System.Collections.Generic.IReadOnlyList<IParseNode> Children => Message.Children;

        public bool IsStateless => Message.IsStateless;

        public CompareOp FilterOp => Message.FilterOp;

        public CompareOp InvertFilterOp => Message.InvertFilterOp;

        public IComparisonParseNode Message => (IComparisonParseNode)inherit_;
    }
}