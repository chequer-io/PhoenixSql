namespace PhoenixSql
{
    public partial class I_BindableStatement : IBindableStatement
    {
        public int BindCount => Value.BindCount;

        public Operation Operation => Value.Operation;

        public IBindableStatement Value => (IBindableStatement)inherit_;
    }
}
