using System.Collections.Generic;

namespace PhoenixSql
{
    public interface IParseNode
    {
        IReadOnlyList<IParseNode> Children { get; }

        bool IsStateless { get; }
    }
}
