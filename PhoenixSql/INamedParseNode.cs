namespace PhoenixSql
{
    public interface INamedParseNode : ITerminalParseNode
    {
        NamedNode NamedNode { get; }
    }
}
