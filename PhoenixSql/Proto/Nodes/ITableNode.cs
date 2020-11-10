namespace PhoenixSql
{
    public interface ITableNode : IPhoenixNode
    {
        string Alias { get; }
    }
}
