namespace PhoenixSql
{
    public partial class ColumnParseNode : INamedParseNode
    {
        System.Collections.Generic.IReadOnlyList<IParseNode> IParseNode.Children => Children;
    }
}
