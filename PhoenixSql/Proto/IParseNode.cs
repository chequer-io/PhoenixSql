using System.Collections.Generic;

namespace PhoenixSql.Proto
{
    public interface IParseNode
    {
        IReadOnlyList<IParseNode> Children { get; }

        bool IsStateless { get; }
    }
}
