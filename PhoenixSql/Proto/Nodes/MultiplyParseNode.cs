namespace PhoenixSql
{
    public partial class MultiplyParseNode : IArithmeticParseNode
    {
        System.Collections.Generic.IReadOnlyList<IParseNode> IParseNode.Children => Children;
    }
}
