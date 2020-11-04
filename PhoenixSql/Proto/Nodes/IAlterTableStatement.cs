namespace PhoenixSql
{
    public interface IAlterTableStatement : ISingleTableStatement
    {
        PTableType TableType { get; }
    }
}
