namespace PhoenixSql
{
    public partial class EqualParseNode : IComparisonParseNode
    {
        System.Collections.Generic.IReadOnlyList<IParseNode> IParseNode.Children => Children;
    }
}
