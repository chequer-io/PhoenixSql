using System;

namespace PhoenixSql.Extensions
{
    public static class JoinTypeExtension
    {
        public static string ToSql(this JoinType type)
        {
            return type switch
            {
                JoinType.Inner => "INNER",
                JoinType.Left => "LEFT",
                JoinType.Right => "RIGHT",
                JoinType.Full => "FULL",
                JoinType.Semi => "SEMI",
                JoinType.Anti => "ANTI",
                _ => throw new ArgumentOutOfRangeException()
            };
        }
    }
}
