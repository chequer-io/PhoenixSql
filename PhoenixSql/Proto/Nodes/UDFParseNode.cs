namespace PhoenixSql
{
    public partial class UDFParseNode : IFunctionParseNode
    {
        System.Collections.Generic.IReadOnlyList<IParseNode> IParseNode.Children => Children;
    }
}
