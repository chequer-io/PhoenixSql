namespace PhoenixSql
{
    public partial class SelectStatement : IFilterableStatement
    {
        IParseNode IFilterableStatement.Where => Where;
    }
}
