namespace PhoenixSql
{
    public partial class MinAggregateParseNode : IDelegateConstantToCountParseNode
    {
        System.Collections.Generic.IReadOnlyList<IParseNode> IParseNode.Children => Children;
    }
}
