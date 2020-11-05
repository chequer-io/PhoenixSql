namespace PhoenixSql
{
    public partial class P_TableNode : IProxyMessage<ITableNode>, ITableNode
    {
        public string Alias => Message.Alias;

        public ITableNode Message => (ITableNode)inherit_;
    }
}