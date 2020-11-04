namespace PhoenixSql
{
    public partial class ArrayConstructorNode : ICompoundParseNode
    {
        System.Collections.Generic.IReadOnlyList<IParseNode> IParseNode.Children => Children;
    }
}
