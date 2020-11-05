namespace PhoenixSql
{
    public partial class W_TableNode : ITableNode
    {
        public string Alias => Value.Alias;

        public ITableNode Value => (ITableNode)inherit_;
    }
}
