namespace PhoenixSql
{
    public partial class AndParseNode : ICompoundParseNode
    {
        System.Collections.Generic.IReadOnlyList<IParseNode> IParseNode.Children => Children;
    }
}
