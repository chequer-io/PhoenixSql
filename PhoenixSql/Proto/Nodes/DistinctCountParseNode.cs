namespace PhoenixSql
{
    public partial class DistinctCountParseNode : IDelegateConstantToCountParseNode
    {
        System.Collections.Generic.IReadOnlyList<IParseNode> IParseNode.Children => Children;
    }
}
