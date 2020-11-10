using System;

namespace PhoenixSql.Extensions
{
    public static class LiteralParseNodeExtension
    {
        public static bool IsNull(this LiteralParseNode node)
        {
            return
                node.Type == default &&
                string.IsNullOrEmpty(node.Value);
        }

        public static bool IsTrue(this LiteralParseNode node)
        {
            return
                node.Type == PDataType.Boolean &&
                node.Value.Equals("true", StringComparison.OrdinalIgnoreCase);
        }
    }
}
