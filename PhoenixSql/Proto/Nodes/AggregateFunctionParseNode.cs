namespace PhoenixSql
{
    public partial class AggregateFunctionParseNode : IAggregateFunctionParseNode
    {
        System.Collections.Generic.IReadOnlyList<IParseNode> IParseNode.Children => Children;
    }
}