using System.Threading.Tasks;
using PhoenixSql.Internal;

namespace PhoenixSql
{
    public sealed class PhoenixSqlParser
    {
        public static IBindableStatement Parse(string sql)
        {
            return PhoenixSqlParserHost.Instance.Parse(sql);
        }

        public static Task<IBindableStatement> ParseAsync(string sql)
        {
            return PhoenixSqlParserHost.Instance.ParseAsync(sql);
        }
    }
}


