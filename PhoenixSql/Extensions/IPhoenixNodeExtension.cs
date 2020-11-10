namespace PhoenixSql.Extensions
{
    public static class IPhoenixNodeExtension
    {
        public static IPhoenixNode Unwrap(this IPhoenixNode node)
        {
            while (node is IPhoenixProxyNode<IPhoenixNode> proxy)
            {
                node = proxy.Message;
            }

            return node;
        }

        public static T UnwrapAs<T>(this IPhoenixNode node) where T : IPhoenixNode
        {
            return (T)node.Unwrap();
        }
    }
}
