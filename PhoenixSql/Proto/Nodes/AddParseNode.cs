namespace PhoenixSql
{
    public partial class AddParseNode : IArithmeticParseNode
    {
        System.Collections.Generic.IReadOnlyList<IParseNode> IParseNode.Children => Children;
    }
}
