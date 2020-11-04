namespace PhoenixSql
{
    public interface INamedNode
    {
        string Name { get; }

        bool IsCaseSensitive { get; }
    }
}
