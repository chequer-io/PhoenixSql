namespace PhoenixSql
{
    public partial class InParseNode : IBinaryParseNode
    {
        System.Collections.Generic.IReadOnlyList<IParseNode> IParseNode.Children => Children;
    }
}
