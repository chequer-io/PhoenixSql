namespace PhoenixSql
{
    public partial class InListParseNode : ICompoundParseNode
    {
        System.Collections.Generic.IReadOnlyList<IParseNode> IParseNode.Children => Children;
    }
}
