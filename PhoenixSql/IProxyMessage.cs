namespace PhoenixSql
{
    public interface IProxyMessage<out TMessage>
    {
        TMessage Message { get; }
    }
}
