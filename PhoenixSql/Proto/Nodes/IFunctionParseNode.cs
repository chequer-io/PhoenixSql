using System.Collections.Generic;

namespace PhoenixSql
{
    public interface IFunctionParseNode : ICompoundParseNode
    {
        string Name { get; }

        bool IsAggregate { get; }
    }
}
