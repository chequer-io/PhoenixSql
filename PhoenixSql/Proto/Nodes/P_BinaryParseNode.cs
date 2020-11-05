namespace PhoenixSql
{
    public partial class P_BinaryParseNode : IProxyMessage<IBinaryParseNode>, IBinaryParseNode
    {
        public string Alias => Message.Alias;

        public System.Collections.Generic.IReadOnlyList<IParseNode> Children => Message.Children;

        public bool IsStateless => Message.IsStateless;

        public IBinaryParseNode Message => (IBinaryParseNode)inherit_;
    }
}