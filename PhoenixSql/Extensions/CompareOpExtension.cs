using System;

namespace PhoenixSql.Extensions
{
    public static class CompareOpExtension
    {
        public static string ToSql(this CompareOp op)
        {
            return op switch
            {
                CompareOp.Equal => "=",
                CompareOp.Less => "<",
                CompareOp.LessOrEqual => "<=",
                CompareOp.NotEqual => "!=",
                CompareOp.GreaterOrEqual => ">=",
                CompareOp.Greater => ">",
                _ => throw new ArgumentOutOfRangeException()
            };
        }
    }
}
