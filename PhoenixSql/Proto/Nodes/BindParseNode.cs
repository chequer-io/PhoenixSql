namespace PhoenixSql
{
    public partial class BindParseNode : INamedParseNode
    {
        System.Collections.Generic.IReadOnlyList<IParseNode> IParseNode.Children => Children;
    }
}
