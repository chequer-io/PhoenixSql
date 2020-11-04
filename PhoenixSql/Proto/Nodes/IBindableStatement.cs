namespace PhoenixSql
{
    public interface IBindableStatement
    {
        int BindCount { get; }

        Operation Operation { get; }
    }
}
