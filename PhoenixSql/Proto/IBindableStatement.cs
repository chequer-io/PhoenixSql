namespace PhoenixSql.Proto
{
    // apache/phoenix/parse/BindableStatement.java
    public interface IBindableStatement
    {
        int BindCount { get; }

        StatementOperation Operation { get; }
    }
}
