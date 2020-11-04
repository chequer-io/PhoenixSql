using System.Collections.Generic;

namespace PhoenixSql
{
    public partial class DeleteStatement : IDMLStatement
    {
        IReadOnlyList<UDFMapEntry> IDMLStatement.UdfParseNodes => udfParseNodes_;
    }
}
