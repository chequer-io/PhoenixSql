using System.Collections.Generic;

namespace PhoenixSql
{
    public interface IArrayAllAnyComparisonNode : ICompoundParseNode
    {
        string Type { get; }
    }
}
