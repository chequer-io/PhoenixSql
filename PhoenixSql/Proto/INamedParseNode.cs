namespace PhoenixSql.Proto
{
    public interface INamedParseNode : ITerminalParseNode
    {
        NamedNode NamedNode { get; }
    }
}
