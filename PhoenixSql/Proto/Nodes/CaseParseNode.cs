namespace PhoenixSql
{
    public partial class CaseParseNode : ICompoundParseNode
    {
        System.Collections.Generic.IReadOnlyList<IParseNode> IParseNode.Children => Children;
    }
}
