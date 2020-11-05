namespace PhoenixSql
{
    public interface IBinaryParseNode : ICompoundParseNode
    {
        public IParseNode LHS => Children[0];

        public IParseNode RHS => Children[1];
    }
}
