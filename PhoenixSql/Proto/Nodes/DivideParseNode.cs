namespace PhoenixSql
{
    public partial class DivideParseNode : IArithmeticParseNode
    {
        System.Collections.Generic.IReadOnlyList<IParseNode> IParseNode.Children => Children;
    }
}
