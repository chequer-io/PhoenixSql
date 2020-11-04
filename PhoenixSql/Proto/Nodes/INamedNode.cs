using System.Collections.Generic;

namespace PhoenixSql
{
    public interface INamedNode
    {
        string Name { get; }

        bool IsCaseSensitive { get; }
    }
}
