namespace PhoenixSql
{
    public partial class ArrayElemRefNode : ICompoundParseNode
    {
        System.Collections.Generic.IReadOnlyList<IParseNode> IParseNode.Children => Children;
    }
}
