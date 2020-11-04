using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class SelectStatement : IFilterableStatement
    {
        IParseNode IFilterableStatement.Where => where_;

        IReadOnlyList<OrderByNode> IFilterableStatement.OrderBy => orderBy_;
    }
}
