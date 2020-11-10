using System.Collections.Generic;

namespace PhoenixSql.Utilities
{
    public static class PhoenixDataTypeUtility
    {
        private static readonly Dictionary<PDataType, string> _typeNames =
            new Dictionary<PDataType, string>
            {
                [PDataType.Varchar] = "VARCHAR",
                [PDataType.Char] = "CHAR",
                [PDataType.Long] = "BIGINT",
                [PDataType.Integer] = "INTEGER",
                [PDataType.Smallint] = "SMALLINT",
                [PDataType.Tinyint] = "TINYINT",
                [PDataType.Float] = "FLOAT",
                [PDataType.Double] = "DOUBLE",
                [PDataType.Decimal] = "DECIMAL",
                [PDataType.Timestamp] = "TIMESTAMP",
                [PDataType.Time] = "TIME",
                [PDataType.Date] = "DATE",
                [PDataType.UnsignedTimestamp] = "UNSIGNED_TIMESTAMP",
                [PDataType.UnsignedTime] = "UNSIGNED_TIME",
                [PDataType.UnsignedDate] = "UNSIGNED_DATE",
                [PDataType.UnsignedLong] = "UNSIGNED_LONG",
                [PDataType.UnsignedInt] = "UNSIGNED_INT",
                [PDataType.UnsignedSmallint] = "UNSIGNED_SMALLINT",
                [PDataType.UnsignedTinyint] = "UNSIGNED_TINYINT",
                [PDataType.UnsignedFloat] = "UNSIGNED_FLOAT",
                [PDataType.UnsignedDouble] = "UNSIGNED_DOUBLE",
                [PDataType.Boolean] = "BOOLEAN",
                [PDataType.Varbinary] = "VARBINARY",
                [PDataType.Binary] = "BINARY",
                [PDataType.IntegerArray] = "INTEGER ARRAY",
                [PDataType.BooleanArray] = "BOOLEAN ARRAY",
                [PDataType.VarcharArray] = "VARCHAR ARRAY",
                [PDataType.VarbinaryArray] = "VARBINARY ARRAY",
                [PDataType.BinaryArray] = "BINARY ARRAY",
                [PDataType.CharArray] = "CHAR ARRAY",
                [PDataType.LongArray] = "BIGINT ARRAY",
                [PDataType.SmallintArray] = "SMALLINT ARRAY",
                [PDataType.TinyintArray] = "TINYINT ARRAY",
                [PDataType.FloatArray] = "FLOAT ARRAY",
                [PDataType.DoubleArray] = "DOUBLE ARRAY",
                [PDataType.DecimalArray] = "DECIMAL ARRAY",
                [PDataType.TimestampArray] = "TIMESTAMP ARRAY",
                [PDataType.UnsignedTimestampArray] = "UNSIGNED_TIMESTAMP ARRAY",
                [PDataType.TimeArray] = "TIME ARRAY",
                [PDataType.UnsignedTimeArray] = "UNSIGNED_TIME ARRAY",
                [PDataType.DateArray] = "DATE ARRAY",
                [PDataType.UnsignedDateArray] = "UNSIGNED_DATE ARRAY",
                [PDataType.UnsignedLongArray] = "UNSIGNED_LONG ARRAY",
                [PDataType.UnsignedIntArray] = "UNSIGNED_INT ARRAY",
                [PDataType.UnsignedSmallintArray] = "UNSIGNED_SMALLINT ARRAY",
                [PDataType.UnsignedTinyintArray] = "UNSIGNED_TINYINT ARRAY",
                [PDataType.UnsignedFloatArray] = "UNSIGNED_FLOAT ARRAY",
                [PDataType.UnsignedDoubleArray] = "UNSIGNED_DOUBLE ARRAY"
            };

        public static string GetSqlTypeName(PDataType type)
        {
            return _typeNames[type];
        }
    }
}
