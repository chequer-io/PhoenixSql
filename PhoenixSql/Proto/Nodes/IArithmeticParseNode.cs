using System.Collections.Generic;

namespace PhoenixSql
{
    public interface IArithmeticParseNode : ICompoundParseNode
    {
        string Operator { get; }
    }
}
