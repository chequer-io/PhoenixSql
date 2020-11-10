namespace PhoenixSql
{
    public partial class P_ConcreteTableNode : IPhoenixProxyNode<IConcreteTableNode>, IConcreteTableNode
    {
        public string Alias => Message.Alias;

        public TableName Name => Message.Name;

        public double TableSamplingRate => Message.TableSamplingRate;

        public IConcreteTableNode Message => (IConcreteTableNode)inherit_;
    }
}