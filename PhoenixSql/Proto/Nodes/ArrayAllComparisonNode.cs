namespace PhoenixSql
{
    public partial class ArrayAllComparisonNode : IArrayAllAnyComparisonNode
    {
        System.Collections.Generic.IReadOnlyList<IParseNode> IParseNode.Children => Children;
    }
}
