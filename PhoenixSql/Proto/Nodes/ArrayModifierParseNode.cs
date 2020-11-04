namespace PhoenixSql
{
    public partial class ArrayModifierParseNode : IFunctionParseNode
    {
        System.Collections.Generic.IReadOnlyList<IParseNode> IParseNode.Children => Children;
    }
}
