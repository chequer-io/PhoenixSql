namespace PhoenixSql
{
    public interface IPhoenixProxyNode<out TMessage> : IPhoenixNode where TMessage : IPhoenixNode
    {
        TMessage Message { get; }
    }
}
