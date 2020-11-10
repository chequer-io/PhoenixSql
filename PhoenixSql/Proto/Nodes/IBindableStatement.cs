namespace PhoenixSql
{
    public interface IBindableStatement : IPhoenixNode
    {
        int BindCount { get; }

        Operation Operation { get; }
    }
}
