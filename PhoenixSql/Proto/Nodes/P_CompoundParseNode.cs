namespace PhoenixSql
{
    public partial class P_CompoundParseNode : IProxyMessage<ICompoundParseNode>, ICompoundParseNode
    {
        public string Alias => Message.Alias;

        public System.Collections.Generic.IReadOnlyList<IParseNode> Children => Message.Children;

        public bool IsStateless => Message.IsStateless;

        public ICompoundParseNode Message => (ICompoundParseNode)inherit_;
    }
}