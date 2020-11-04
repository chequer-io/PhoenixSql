namespace PhoenixSql
{
    public partial class SubqueryParseNode : ITerminalParseNode
    {
        System.Collections.Generic.IReadOnlyList<IParseNode> IParseNode.Children => Children;
    }
}
