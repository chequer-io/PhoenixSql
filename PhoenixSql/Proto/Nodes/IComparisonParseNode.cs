namespace PhoenixSql
{
    public interface IComparisonParseNode : IBinaryParseNode
    {
        CompareOp FilterOp { get; }

        CompareOp InvertFilterOp { get; }
    }
}
