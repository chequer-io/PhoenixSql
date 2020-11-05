namespace PhoenixSql
{
    public partial class P_AlterTableStatement : IProxyMessage<IAlterTableStatement>, IAlterTableStatement
    {
        public int BindCount => Message.BindCount;

        public Operation Operation => Message.Operation;

        public NamedTableNode Table => Message.Table;

        public PTableType TableType => Message.TableType;

        public IAlterTableStatement Message => (IAlterTableStatement)inherit_;
    }
}