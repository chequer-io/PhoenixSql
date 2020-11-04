using System.Collections.Generic;

namespace PhoenixSql
{
    public interface IConcreteTableNode : ITableNode
    {
        TableName Name { get; }

        double TableSamplingRate { get; }
    }
}
