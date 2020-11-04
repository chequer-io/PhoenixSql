using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class UpsertStatement : IDMLStatement
    {
        IReadOnlyList<UDFMapEntry> IDMLStatement.UdfParseNodes => udfParseNodes_;
    }
}
