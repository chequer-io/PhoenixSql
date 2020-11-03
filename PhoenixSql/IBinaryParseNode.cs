namespace PhoenixSql
{
    public interface IBinaryParseNode : ICompoundParseNode
    {
        public IParseNode Left => Children[0];

        public IParseNode Right => Children[1];
    }
}
