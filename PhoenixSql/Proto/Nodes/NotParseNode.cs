namespace PhoenixSql
{
    public partial class NotParseNode : IUnaryParseNode
    {
        System.Collections.Generic.IReadOnlyList<IParseNode> IParseNode.Children => Children;
    }
}
