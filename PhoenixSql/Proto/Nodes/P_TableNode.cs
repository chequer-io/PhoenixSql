namespace PhoenixSql
{
    public partial class P_TableNode : IPhoenixProxyNode<ITableNode>, ITableNode
    {
        public string Alias => Message.Alias;

        public ITableNode Message => (ITableNode)inherit_;
    }
}