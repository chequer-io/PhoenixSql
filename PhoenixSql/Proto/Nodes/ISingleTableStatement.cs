using System.Collections.Generic;

namespace PhoenixSql
{
    public interface ISingleTableStatement : IMutableStatement
    {
        NamedTableNode Table { get; }
    }
}
