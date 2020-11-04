namespace PhoenixSql
{
    public partial class LiteralParseNode : ITerminalParseNode
    {
        System.Collections.Generic.IReadOnlyList<IParseNode> IParseNode.Children => Children;
    }
}
