using System.Collections.Generic;

namespace PhoenixSql
{
    public interface INamedParseNode : ITerminalParseNode
    {
        string Name { get; }

        bool IsCaseSensitive { get; }
    }
}
