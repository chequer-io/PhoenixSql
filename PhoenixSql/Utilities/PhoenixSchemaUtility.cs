using System;
using System.Collections.Generic;
using System.Text;

namespace PhoenixSql.Utilities
{
    public static class PhoenixSchemaUtility
    {
        private const char escapeCharacter = '"';
        private const char namespaceSeparator = ':';
        private const char nameSeparator = '.';
        private const string schemaForDefaultNamespace = "DEFAULT";
        private const string hbaseNamespace = "HBASE";

        private static readonly HashSet<string> schemaNameBlackList = new HashSet<string>
        {
            schemaForDefaultNamespace,
            hbaseNamespace
        };

        public static bool IsValidSchemaName(string schemaName)
        {
            return !schemaNameBlackList.Contains(schemaName);
        }

        public static string NormalizeIdentifier(string name)
        {
            if (string.IsNullOrEmpty(name))
                return name;

            if (IsCaseSensitive(name))
                return name[1..^1];

            return name.ToUpper();
        }

        public static string NormalizeLiteral(LiteralParseNode literal)
        {
            if (literal == null)
                return null;

            if (IsEnclosedInSingleQuotes(literal.Value))
                return literal.Value[1..^1];

            return literal.Value;
        }

        public static string NormalizeFullTableName(string fullTableName)
        {
            var schemaName = GetSchemaNameFromFullName(fullTableName);
            var tableName = GetTableNameFromFullName(fullTableName);
            var buidler = new StringBuilder();

            if (!string.IsNullOrEmpty(schemaName))
                buidler.Append(NormalizeIdentifier(schemaName)).Append(nameSeparator);

            buidler.Append(NormalizeIdentifier(tableName));

            return buidler.ToString();
        }

        public static string GetUnEscapedFullColumnName(string fullColumnName)
        {
            if (fullColumnName == null)
                throw new ArgumentNullException(nameof(fullColumnName));

            return fullColumnName.Replace(escapeCharacter.ToString(), string.Empty).Trim();
        }

        public static string GetEscapedFullColumnName(string fullColumnName)
        {
            if (fullColumnName[0] == escapeCharacter)
                return fullColumnName;

            int index = fullColumnName.IndexOf(nameSeparator);

            if (index < 0)
                return GetEscapedArgument(fullColumnName);

            var columnFamily = fullColumnName[..index];
            var columnName = fullColumnName[(index + 1)..];

            return $"{GetEscapedArgument(columnFamily)}{nameSeparator}{GetEscapedArgument(columnName)}";
        }

        public static string GetEscapedArgument(string argument)
        {
            if (argument == null)
                throw new ArgumentNullException(nameof(argument));

            return $"{escapeCharacter}{argument}{escapeCharacter}";
        }

        public static string GetSchemaNameFromFullName(string tableName)
        {
            if (IsExistingTableMappedToPhoenixName(tableName))
                return string.Empty;

            if (tableName.Contains(namespaceSeparator))
                return GetSchemaNameFromFullName(tableName, namespaceSeparator);

            return GetSchemaNameFromFullName(tableName, nameSeparator);
        }

        public static string GetSchemaNameFromFullName(string tableName, char separator)
        {
            int index = tableName.IndexOf(separator);

            if (index < 0)
                return string.Empty;

            return tableName[..index];
        }

        public static string GetTableNameFromFullName(string tableName)
        {
            if (IsExistingTableMappedToPhoenixName(tableName))
                return tableName;

            if (tableName.Contains(namespaceSeparator))
            {
                return GetTableNameFromFullName(tableName, namespaceSeparator);
            }

            return GetTableNameFromFullName(tableName, namespaceSeparator);
        }

        public static string GetTableNameFromFullName(string tableName, char separator)
        {
            int index = tableName.IndexOf(separator);

            if (index < 0)
                return tableName;

            return tableName[(index + 1)..];
        }

        public static string GetQualifiedTableName(string schemaName, string tableName)
        {
            if (!string.IsNullOrEmpty(schemaName))
                return $"{NormalizeIdentifier(schemaName)}.{NormalizeIdentifier(tableName)}";

            return NormalizeIdentifier(tableName);
        }

        public static bool IsEnclosedInSingleQuotes(string name)
        {
            return name?.Length > 0 && name[0] == '\'' && name[^1] == '\'';
        }

        public static bool IsCaseSensitive(string name)
        {
            return name?.Length > 0 && name[0] == '"' && name[^1] == '"';
        }

        public static bool IsExistingTableMappedToPhoenixName(string name)
        {
            return name?.Length > 0 && name[0] == '"' && name.IndexOf('"', 1) == name.Length - 1;
        }
    }
}
