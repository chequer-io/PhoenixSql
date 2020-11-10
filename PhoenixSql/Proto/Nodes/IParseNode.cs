namespace PhoenixSql
{
    public interface IParseNode : IPhoenixNode
    {
        string Alias { get; }

        System.Collections.Generic.IReadOnlyList<IParseNode> Children { get; }

        bool IsStateless { get; }
    }
}
