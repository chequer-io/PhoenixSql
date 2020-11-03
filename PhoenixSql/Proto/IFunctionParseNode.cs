namespace PhoenixSql.Proto
{
    public interface IFunctionParseNode : ICompoundParseNode
    {
        string Name { get; }

        BuiltInFunctionInfo Info { get; }
    }
}
