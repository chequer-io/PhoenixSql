namespace PhoenixSql
{
    public partial class CastParseNode : IUnaryParseNode
    {
        System.Collections.Generic.IReadOnlyList<IParseNode> IParseNode.Children => Children;
    }
}
