namespace PhoenixSql
{
    public partial class I_BindableStatement : IProxyMessage<IBindableStatement>, IBindableStatement
    {
        public int BindCount => Message.BindCount;

        public Operation Operation => Message.Operation;

        public IBindableStatement Message => (IBindableStatement)inherit_;
    }
}