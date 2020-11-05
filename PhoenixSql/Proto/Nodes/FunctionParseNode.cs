namespace PhoenixSql
{
    public partial class FunctionParseNode : IFunctionParseNode
    {
        System.Collections.Generic.IReadOnlyList<IParseNode> IParseNode.Children => Children;
    }
}