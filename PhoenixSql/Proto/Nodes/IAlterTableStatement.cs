using System.Collections.Generic;

namespace PhoenixSql
{
    public interface IAlterTableStatement : ISingleTableStatement
    {
        PTableType TableType { get; }
    }
}
