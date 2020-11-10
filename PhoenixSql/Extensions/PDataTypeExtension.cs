using System;
using PhoenixSql.Utilities;

namespace PhoenixSql.Extensions
{
    public static class PDataTypeExtension
    {
        public static string ToSqlTypeName(this PDataType type)
        {
            return PhoenixDataTypeUtility.GetSqlTypeName(type);
        }

        public static bool IsNumeric(this PDataType type)
        {
            return
                type == PDataType.Long ||
                type == PDataType.Integer ||
                type == PDataType.Smallint ||
                type == PDataType.Tinyint ||
                type == PDataType.Float ||
                type == PDataType.Double ||
                type == PDataType.Decimal ||
                type == PDataType.UnsignedLong ||
                type == PDataType.UnsignedInt ||
                type == PDataType.UnsignedSmallint ||
                type == PDataType.UnsignedTinyint ||
                type == PDataType.UnsignedFloat ||
                type == PDataType.UnsignedDouble;
        }

        public static bool IsBinary(this PDataType type)
        {
            return type == PDataType.Varbinary || type == PDataType.Binary;
        }

        public static bool IsArray(this PDataType type)
        {
            return
                type == PDataType.IntegerArray ||
                type == PDataType.BooleanArray ||
                type == PDataType.VarcharArray ||
                type == PDataType.VarbinaryArray ||
                type == PDataType.BinaryArray ||
                type == PDataType.CharArray ||
                type == PDataType.LongArray ||
                type == PDataType.SmallintArray ||
                type == PDataType.TinyintArray ||
                type == PDataType.FloatArray ||
                type == PDataType.DoubleArray ||
                type == PDataType.DecimalArray ||
                type == PDataType.TimestampArray ||
                type == PDataType.UnsignedTimestampArray ||
                type == PDataType.TimeArray ||
                type == PDataType.UnsignedTimeArray ||
                type == PDataType.DateArray ||
                type == PDataType.UnsignedDateArray ||
                type == PDataType.UnsignedLongArray ||
                type == PDataType.UnsignedIntArray ||
                type == PDataType.UnsignedSmallintArray ||
                type == PDataType.UnsignedTinyintArray ||
                type == PDataType.UnsignedFloatArray ||
                type == PDataType.UnsignedDoubleArray;
        }

        public static bool IsRealNumber(this PDataType type)
        {
            return
                type == PDataType.Float ||
                type == PDataType.Double ||
                type == PDataType.Decimal ||
                type == PDataType.UnsignedFloat ||
                type == PDataType.UnsignedDouble;
        }

        public static bool IsWholeNumber(this PDataType type)
        {
            return
                type == PDataType.Long ||
                type == PDataType.Integer ||
                type == PDataType.Smallint ||
                type == PDataType.Tinyint ||
                type == PDataType.UnsignedLong ||
                type == PDataType.UnsignedInt ||
                type == PDataType.UnsignedSmallint ||
                type == PDataType.UnsignedTinyint;
        }

        public static PDataType GetElementType(this PDataType type)
        {
            return type switch
            {
                PDataType.IntegerArray => PDataType.Integer,
                PDataType.BooleanArray => PDataType.Boolean,
                PDataType.VarcharArray => PDataType.Varchar,
                PDataType.VarbinaryArray => PDataType.Varbinary,
                PDataType.BinaryArray => PDataType.Binary,
                PDataType.CharArray => PDataType.Char,
                PDataType.LongArray => PDataType.Long,
                PDataType.SmallintArray => PDataType.Smallint,
                PDataType.TinyintArray => PDataType.Tinyint,
                PDataType.FloatArray => PDataType.Float,
                PDataType.DoubleArray => PDataType.Double,
                PDataType.DecimalArray => PDataType.Decimal,
                PDataType.TimestampArray => PDataType.Timestamp,
                PDataType.UnsignedTimestampArray => PDataType.UnsignedTimestamp,
                PDataType.TimeArray => PDataType.Time,
                PDataType.UnsignedTimeArray => PDataType.UnsignedTime,
                PDataType.DateArray => PDataType.Date,
                PDataType.UnsignedDateArray => PDataType.UnsignedDate,
                PDataType.UnsignedLongArray => PDataType.UnsignedLong,
                PDataType.UnsignedIntArray => PDataType.UnsignedInt,
                PDataType.UnsignedSmallintArray => PDataType.UnsignedSmallint,
                PDataType.UnsignedTinyintArray => PDataType.UnsignedTinyint,
                PDataType.UnsignedFloatArray => PDataType.UnsignedFloat,
                PDataType.UnsignedDoubleArray => PDataType.UnsignedDouble,
                _ => throw new ArgumentOutOfRangeException()
            };
        }
    }
}
