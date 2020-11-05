using System;
using System.Runtime.Serialization;
using PhoenixSql.Internal;

namespace PhoenixSql
{
    public abstract class PhoenixSqlException : Exception
    {
        protected PhoenixSqlException()
        {
        }

        protected PhoenixSqlException(SerializationInfo info, StreamingContext context) : base(info, context)
        {
        }

        protected PhoenixSqlException(string message) : base(message)
        {
        }

        protected PhoenixSqlException(string message, Exception innerException) : base(message, innerException)
        {
        }
    }

    public sealed class PhoenixSqlSyntaxException : PhoenixSqlException
    {
        internal PhoenixSqlSyntaxException(string message) : base(message)
        {
        }
    }

    public sealed class PhoenixSqlHostException : PhoenixSqlException
    {
        internal PhoenixSqlHostException(string message) : base(message)
        {
        }

        internal PhoenixSqlHostException(string message, Exception exception) : base(message, exception)
        {
        }
    }
}
