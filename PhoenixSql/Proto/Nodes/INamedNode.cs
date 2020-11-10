namespace PhoenixSql
{
    public interface INamedNode : IPhoenixNode
    {
        string Name { get; }

        bool IsCaseSensitive { get; }
    }
}
