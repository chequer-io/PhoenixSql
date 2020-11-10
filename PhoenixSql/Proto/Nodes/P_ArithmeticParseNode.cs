namespace PhoenixSql
{
    public partial class P_ArithmeticParseNode : IPhoenixProxyNode<IArithmeticParseNode>, IArithmeticParseNode
    {
        public string Alias => Message.Alias;

        public System.Collections.Generic.IReadOnlyList<IParseNode> Children => Message.Children;

        public bool IsStateless => Message.IsStateless;

        public string Operator => Message.Operator;

        public IArithmeticParseNode Message => (IArithmeticParseNode)inherit_;
    }
}