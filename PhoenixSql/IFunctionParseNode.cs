namespace PhoenixSql
{
    public interface IFunctionParseNode : ICompoundParseNode
    {
        string Name { get; }

        BuiltInFunctionInfo Info { get; }
    }
}
