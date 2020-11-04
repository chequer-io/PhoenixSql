using System.Collections.Generic;

namespace PhoenixSql
{
    public interface IDMLStatement : ISingleTableStatement
    {
        IReadOnlyList<UDFMapEntry> UdfParseNodes { get; }
    }
}
