// ReSharper disable UnusedParameter.Local

using System;
using System.Linq;
using System.Text.RegularExpressions;
using PhoenixSql.Extensions;
using PhoenixSql.Internal;
using PhoenixSql.Utilities;

namespace PhoenixSql
{
    public static class PhoenixSqlDeparser
    {
        private static readonly Regex _namePattern = new Regex(@"^[A-Z_][A-Z\d_]*$");

        public static string Deparse(IPhoenixNode node)
        {
            var writer = new ScriptWriter();

            switch (node.Unwrap())
            {
                case IParseNode iParseNode:
                    DeparseParseNode(writer, iParseNode);
                    break;

                case INamedNode namedNode:
                    DeparseNamedNode(writer, namedNode);
                    break;

                case IBindableStatement iBindableStatement:
                    DeparseBindableStatement(writer, iBindableStatement);
                    break;

                case ITableNode iTableNode:
                    DeparseTableNode(writer, iTableNode);
                    break;

                case PName pName:
                    DeparsePName(writer, pName);
                    break;

                case PTableKey pTableKey:
                    DeparsePTableKey(writer, pTableKey);
                    break;

                case OrderByNode orderByNode:
                    DeparseOrderByNode(writer, orderByNode);
                    break;

                case HintNode hintNode:
                    DeparseHintNode(writer, hintNode);
                    break;

                case ColumnDef columnDef:
                    DeparseColumnDef(writer, columnDef);
                    break;

                case OffsetNode offsetNode:
                    DeparseOffsetNode(writer, offsetNode);
                    break;

                case PFunction pFunction:
                    DeparsePFunction(writer, pFunction);
                    break;

                case LimitNode limitNode:
                    DeparseLimitNode(writer, limitNode);
                    break;

                case IndexKeyConstraint indexKeyConstraint:
                    DeparseIndexKeyConstraint(writer, indexKeyConstraint);
                    break;

                case CursorName cursorName:
                    DeparseCursorName(writer, cursorName);
                    break;

                case ColumnName columnName:
                    DeparseColumnName(writer, columnName);
                    break;

                case FunctionArgument functionArgument:
                    DeparseFunctionArgument(writer, functionArgument);
                    break;

                case AliasedNode aliasedNode:
                    DeparseAliasedNode(writer, aliasedNode);
                    break;

                case TableName tableName:
                    DeparseTableName(writer, tableName);
                    break;

                default:
                    throw new PhoenixSqlDeparserException(node);
            }

            return writer.ToString();
        }

        private static void DeparsePName(ScriptWriter writer, PName node)
        {
            writer.Write(node.Value);
        }

        private static void DeparsePTableKey(ScriptWriter writer, PTableKey node)
        {
            writer.Write(node.Name);

            if (!string.IsNullOrEmpty(node.TenantId?.Value))
            {
                writer.Write(" for ");
                writer.Write(node.TenantId.Value);
            }
        }

        private static void DeparseOrderByNode(ScriptWriter writer, OrderByNode node)
        {
            DeparseParseNode(writer, node.Node);

            if (!node.IsAscending)
                writer.Write(" DESC");

            if (node.IsNullsLast)
                writer.Write(" NULLS LAST ");
        }

        private static void DeparseHintNode(ScriptWriter writer, HintNode node)
        {
            if (node.Hints.Count == 0)
                return;

            writer.Write("/*+ ");

            foreach (var entry in node.Hints)
            {
                writer.Write(entry.Key.ToSql());
                writer.Write(entry.Value);
                writer.WriteSpace();
            }

            writer.Write("*/");
        }

        private static void DeparseColumnDef(ScriptWriter writer, ColumnDef node)
        {
            DeparseNamedNode(writer, node.ColumnDefName.ColumnNode);
            writer.Write(' ');
            writer.Write(node.DataType.ToSqlTypeName());
            WriteTypeSize(writer, node.MaxLength, node.Scale);

            if (node.IsArray)
                writer.Write(" ARRAY ");
        }

        #region IBindableStatement
        private static void DeparseBindableStatement(ScriptWriter writer, I_BindableStatement node)
        {
            DeparseBindableStatement(writer, node.Message);
        }

        private static void DeparseBindableStatement(ScriptWriter writer, IBindableStatement node)
        {
            switch (node.Unwrap())
            {
                case IMutableStatement mutableStatement:
                    DeparseMutableStatement(writer, mutableStatement);
                    break;

                case IFilterableStatement filterableStatement:
                    DeparseFilterableStatement(writer, filterableStatement);
                    break;

                case CloseStatement closeStatement:
                    DeparseCloseStatement(writer, closeStatement);
                    break;

                case ExecuteUpgradeStatement executeUpgradeStatement:
                    DeparseExecuteUpgradeStatement(writer, executeUpgradeStatement);
                    break;

                case OpenStatement openStatement:
                    DeparseOpenStatement(writer, openStatement);
                    break;

                case FetchStatement fetchStatement:
                    DeparseFetchStatement(writer, fetchStatement);
                    break;

                case DeclareCursorStatement declareCursorStatement:
                    DeparseDeclareCursorStatement(writer, declareCursorStatement);
                    break;

                case TraceStatement traceStatement:
                    DeparseTraceStatement(writer, traceStatement);
                    break;

                case ListJarsStatement listJarsStatement:
                    DeparseListJarsStatement(writer, listJarsStatement);
                    break;

                case ChangePermsStatement changePermsStatement:
                    DeparseChangePermsStatement(writer, changePermsStatement);
                    break;

                case ExplainStatement explainStatement:
                    DeparseExplainStatement(writer, explainStatement);
                    break;

                default:
                    throw new PhoenixSqlDeparserException(node);
            }
        }

        private static void DeparseFilterableStatement(ScriptWriter writer, I_FilterableStatement node)
        {
            DeparseFilterableStatement(writer, node.Message);
        }

        private static void DeparseFilterableStatement(ScriptWriter writer, IFilterableStatement node)
        {
            switch (node.Unwrap())
            {
                case SelectStatement selectStatement:
                    DeparseSelectStatement(writer, selectStatement);
                    break;

                default:
                    throw new PhoenixSqlDeparserException(node);
            }
        }

        private static void DeparseMutableStatement(ScriptWriter writer, P_MutableStatement node)
        {
            DeparseMutableStatement(writer, node.Message);
        }

        private static void DeparseMutableStatement(ScriptWriter writer, IMutableStatement node)
        {
            switch (node.Unwrap())
            {
                case ISingleTableStatement singleTableStatement:
                    DeparseSingleTableStatement(writer, singleTableStatement);
                    break;

                case DropTableStatement dropTableStatement:
                    DeparseDropTableStatement(writer, dropTableStatement);
                    break;

                case DropSequenceStatement dropSequenceStatement:
                    DeparseDropSequenceStatement(writer, dropSequenceStatement);
                    break;

                case AlterSessionStatement alterSessionStatement:
                    DeparseAlterSessionStatement(writer, alterSessionStatement);
                    break;

                case CreateSequenceStatement createSequenceStatement:
                    DeparseCreateSequenceStatement(writer, createSequenceStatement);
                    break;

                case CreateFunctionStatement createFunctionStatement:
                    DeparseCreateFunctionStatement(writer, createFunctionStatement);
                    break;

                case DropIndexStatement dropIndexStatement:
                    DeparseDropIndexStatement(writer, dropIndexStatement);
                    break;

                case CreateSchemaStatement createSchemaStatement:
                    DeparseCreateSchemaStatement(writer, createSchemaStatement);
                    break;

                case DropSchemaStatement dropSchemaStatement:
                    DeparseDropSchemaStatement(writer, dropSchemaStatement);
                    break;

                case CreateTableStatement createTableStatement:
                    DeparseCreateTableStatement(writer, createTableStatement);
                    break;

                case AddJarsStatement addJarsStatement:
                    DeparseAddJarsStatement(writer, addJarsStatement);
                    break;

                case DeleteJarStatement deleteJarStatement:
                    DeparseDeleteJarStatement(writer, deleteJarStatement);
                    break;

                case DropFunctionStatement dropFunctionStatement:
                    DeparseDropFunctionStatement(writer, dropFunctionStatement);
                    break;

                case UseSchemaStatement useSchemaStatement:
                    DeparseUseSchemaStatement(writer, useSchemaStatement);
                    break;

                default:
                    throw new PhoenixSqlDeparserException(node);
            }
        }

        private static void DeparseSingleTableStatement(ScriptWriter writer, P_SingleTableStatement node)
        {
            DeparseSingleTableStatement(writer, node.Message);
        }

        private static void DeparseSingleTableStatement(ScriptWriter writer, ISingleTableStatement node)
        {
            switch (node.Unwrap())
            {
                case IDMLStatement dmlStatement:
                    DeparseDMLStatement(writer, dmlStatement);
                    break;

                case IAlterTableStatement alterTableStatement:
                    DeparseAlterTableStatement(writer, alterTableStatement);
                    break;

                case UpdateStatisticsStatement updateStatisticsStatement:
                    DeparseUpdateStatisticsStatement(writer, updateStatisticsStatement);
                    break;

                case CreateIndexStatement createIndexStatement:
                    DeparseCreateIndexStatement(writer, createIndexStatement);
                    break;

                case AlterIndexStatement alterIndexStatement:
                    DeparseAlterIndexStatement(writer, alterIndexStatement);
                    break;

                default:
                    throw new PhoenixSqlDeparserException(node);
            }
        }

        private static void DeparseAlterTableStatement(ScriptWriter writer, P_AlterTableStatement node)
        {
            DeparseAlterTableStatement(writer, node.Message);
        }

        private static void DeparseAlterTableStatement(ScriptWriter writer, IAlterTableStatement node)
        {
            switch (node.Unwrap())
            {
                case AddColumnStatement addColumnStatement:
                    DeparseAddColumnStatement(writer, addColumnStatement);
                    break;

                case DropColumnStatement dropColumnStatement:
                    DeparseDropColumnStatement(writer, dropColumnStatement);
                    break;

                default:
                    throw new PhoenixSqlDeparserException(node);
            }
        }

        private static void DeparseDropTableStatement(ScriptWriter writer, DropTableStatement node)
        {
            throw new NotSupportedException();
        }

        private static void DeparseAddColumnStatement(ScriptWriter writer, AddColumnStatement node)
        {
            throw new NotSupportedException();
        }

        private static void DeparseDropColumnStatement(ScriptWriter writer, DropColumnStatement node)
        {
            throw new NotSupportedException();
        }

        private static void DeparseUpdateStatisticsStatement(ScriptWriter writer, UpdateStatisticsStatement node)
        {
            throw new NotSupportedException();
        }

        private static void DeparseCreateIndexStatement(ScriptWriter writer, CreateIndexStatement node)
        {
            throw new NotSupportedException();
        }

        private static void DeparseDMLStatement(ScriptWriter writer, P_DMLStatement node)
        {
            DeparseDMLStatement(writer, node.Message);
        }

        private static void DeparseDMLStatement(ScriptWriter writer, IDMLStatement node)
        {
            switch (node.Unwrap())
            {
                case DeleteStatement deleteStatement:
                    DeparseDeleteStatement(writer, deleteStatement);
                    break;

                case UpsertStatement upsertStatement:
                    DeparseUpsertStatement(writer, upsertStatement);
                    break;

                default:
                    throw new PhoenixSqlDeparserException(node);
            }
        }

        private static void DeparseDeleteStatement(ScriptWriter writer, DeleteStatement node)
        {
            writer.Write("DELETE ");

            if (node.Hint != null)
            {
                DeparseHintNode(writer, node.Hint);
                writer.WriteSpace();
            }

            writer.Write("FROM ");
            DeparseNamedTableNode(writer, node.Table);

            if (node.Where != null)
            {
                writer.WriteSpace().Write("WHERE ");
                DeparseParseNode(writer, node.Where);
            }

            if (node.OrderBy.Count > 0)
            {
                writer.WriteSpace().Write("ORDER BY ");
                writer.WriteJoin(", ", node.OrderBy, DeparseOrderByNode);
            }

            if (node.Limit != null)
            {
                writer.WriteSpace().Write("LIMIT ");
                DeparseLimitNode(writer, node.Limit);
            }
        }

        private static void DeparseUpsertStatement(ScriptWriter writer, UpsertStatement node)
        {
            writer.Write("UPSERT ");

            if (node.Hint != null)
            {
                DeparseHintNode(writer, node.Hint);
                writer.WriteSpace();
            }

            writer.Write("INTO ");
            DeparseNamedTableNode(writer, node.Table);

            if (node.Columns.Count > 0)
            {
                writer.Write('(');
                writer.WriteJoin(", ", node.Columns, DeparseColumnName);
                writer.Write(')');
            }

            if (node.Values.Count > 0)
            {
                writer.WriteSpace();
                writer.Write("VALUES (");
                writer.WriteJoin(", ", node.Values, DeparseParseNode);
                writer.Write(')');
            }
            else if (node.Select != null)
            {
                writer.WriteSpace();
                DeparseSelectStatement(writer, node.Select);
            }

            if (node.OnDupKeyIgnore)
            {
                writer.WriteSpace();
                writer.Write("ON DUPLICATE KEY IGNORE");
            }
            else if (node.OnDupKeyPairs.Count > 0)
            {
                writer.WriteSpace();
                writer.Write("ON DUPLICATE KEY UPDATE ");

                writer.WriteJoin(", ", node.OnDupKeyPairs, (w, pair) =>
                {
                    DeparseColumnName(w, pair.First);
                    w.WriteSpace().Write("=").WriteSpace();
                    DeparseParseNode(w, pair.Second);
                });
            }
        }

        private static void DeparseAlterIndexStatement(ScriptWriter writer, AlterIndexStatement node)
        {
            throw new NotSupportedException();
        }

        private static void DeparseDropSequenceStatement(ScriptWriter writer, DropSequenceStatement node)
        {
            throw new NotSupportedException();
        }

        private static void DeparseAlterSessionStatement(ScriptWriter writer, AlterSessionStatement node)
        {
            throw new NotSupportedException();
        }

        private static void DeparseCreateSequenceStatement(ScriptWriter writer, CreateSequenceStatement node)
        {
            throw new NotSupportedException();
        }

        private static void DeparseCreateFunctionStatement(ScriptWriter writer, CreateFunctionStatement node)
        {
            throw new NotSupportedException();
        }

        private static void DeparseDropIndexStatement(ScriptWriter writer, DropIndexStatement node)
        {
            throw new NotSupportedException();
        }

        private static void DeparseCreateSchemaStatement(ScriptWriter writer, CreateSchemaStatement node)
        {
            throw new NotSupportedException();
        }

        private static void DeparseDropSchemaStatement(ScriptWriter writer, DropSchemaStatement node)
        {
            throw new NotSupportedException();
        }

        private static void DeparseCreateTableStatement(ScriptWriter writer, CreateTableStatement node)
        {
            throw new NotSupportedException();
        }

        private static void DeparseAddJarsStatement(ScriptWriter writer, AddJarsStatement node)
        {
            throw new NotSupportedException();
        }

        private static void DeparseDeleteJarStatement(ScriptWriter writer, DeleteJarStatement node)
        {
            throw new NotSupportedException();
        }

        private static void DeparseDropFunctionStatement(ScriptWriter writer, DropFunctionStatement node)
        {
            throw new NotSupportedException();
        }

        private static void DeparseUseSchemaStatement(ScriptWriter writer, UseSchemaStatement node)
        {
            throw new NotSupportedException();
        }

        private static void DeparseCloseStatement(ScriptWriter writer, CloseStatement node)
        {
            throw new NotSupportedException();
        }

        private static void DeparseExecuteUpgradeStatement(ScriptWriter writer, ExecuteUpgradeStatement node)
        {
            throw new NotSupportedException();
        }

        private static void DeparseSelectStatement(ScriptWriter writer, SelectStatement node)
        {
            writer.Write("SELECT ");

            if (node.Hint != null)
                DeparseHintNode(writer, node.Hint);

            if (node.IsDistinct)
                writer.Write("DISTINCT ");

            writer.WriteJoin(", ", node.Select, DeparseAliasedNode);

            if (node.From != null)
            {
                writer.WriteSpace().Write("FROM ");
                DeparseTableNode(writer, node.From);
            }

            if (node.Where != null)
            {
                writer.WriteSpace().Write("WHERE ");
                DeparseParseNode(writer, node.Where);
            }

            if (node.GroupBy.Count > 0)
            {
                writer.WriteSpace().Write("GROUP BY ");
                writer.WriteJoin(", ", node.GroupBy, DeparseParseNode);
            }

            if (node.Having != null)
            {
                writer.WriteSpace().Write("HAVING ");
                DeparseParseNode(writer, node.Having);
            }

            if (node.OrderBy.Count > 0)
            {
                writer.WriteSpace().Write("ORDER BY ");
                writer.WriteJoin(", ", node.OrderBy, DeparseOrderByNode);
            }

            if (node.Limit != null)
            {
                writer.WriteSpace().Write("LIMIT ");
                DeparseLimitNode(writer, node.Limit);
            }

            if (node.Offset != null)
            {
                writer.WriteSpace().Write("OFFSET ");
                DeparseOffsetNode(writer, node.Offset);
            }
        }

        private static void DeparseOpenStatement(ScriptWriter writer, OpenStatement node)
        {
            throw new NotSupportedException();
        }

        private static void DeparseFetchStatement(ScriptWriter writer, FetchStatement node)
        {
            throw new NotSupportedException();
        }

        private static void DeparseDeclareCursorStatement(ScriptWriter writer, DeclareCursorStatement node)
        {
            throw new NotSupportedException();
        }

        private static void DeparseTraceStatement(ScriptWriter writer, TraceStatement node)
        {
            throw new NotSupportedException();
        }

        private static void DeparseListJarsStatement(ScriptWriter writer, ListJarsStatement node)
        {
            throw new NotSupportedException();
        }

        private static void DeparseChangePermsStatement(ScriptWriter writer, ChangePermsStatement node)
        {
            throw new NotSupportedException();
        }

        private static void DeparseExplainStatement(ScriptWriter writer, ExplainStatement node)
        {
            throw new NotSupportedException();
        }
        #endregion

        private static void DeparseOffsetNode(ScriptWriter writer, OffsetNode node)
        {
            DeparseParseNode(writer, node.OffsetParseNode);
        }

        private static void DeparsePFunction(ScriptWriter writer, PFunction node)
        {
            throw new NotSupportedException();
        }

        private static void DeparseLimitNode(ScriptWriter writer, LimitNode node)
        {
            DeparseParseNode(writer, node.LimitParseNode);
        }

        private static void DeparseNamedNode(ScriptWriter writer, P_NamedNode node)
        {
            DeparseNamedNode(writer, node.Message);
        }

        private static void DeparseNamedNode(ScriptWriter writer, INamedNode node)
        {
            WriteIdentifier(writer, node.Name, node.IsCaseSensitive);
        }

        private static void DeparseIndexKeyConstraint(ScriptWriter writer, IndexKeyConstraint node)
        {
            throw new NotSupportedException();
        }

        #region ITableNode
        private static void DeparseTableNode(ScriptWriter writer, P_TableNode node)
        {
            DeparseTableNode(writer, node.Message);
        }

        private static void DeparseTableNode(ScriptWriter writer, ITableNode node)
        {
            switch (node.Unwrap())
            {
                case NamedTableNode namedTableNode:
                    DeparseNamedTableNode(writer, namedTableNode);
                    break;

                case BindTableNode bindTableNode:
                    DeparseBindTableNode(writer, bindTableNode);
                    break;

                case JoinTableNode joinTableNode:
                    DeparseJoinTableNode(writer, joinTableNode);
                    break;

                case DerivedTableNode derivedTableNode:
                    DeparseDerivedTableNode(writer, derivedTableNode);
                    break;

                default:
                    throw new PhoenixSqlDeparserException(node);
            }
        }

        private static void DeparseNamedTableNode(ScriptWriter writer, NamedTableNode node)
        {
            DeparseTableName(writer, node.Name);

            if (node.DynamicColumns.Count > 0)
            {
                writer.Write('(');
                writer.WriteJoin(", ", node.DynamicColumns, DeparseColumnDef);
                writer.Write(')');
            }

            if (!string.IsNullOrEmpty(node.Alias))
            {
                writer.WriteSpace();
                writer.Write(node.Alias);
            }

            if (Math.Abs(node.TableSamplingRate) > double.Epsilon)
            {
                writer.WriteSpace();
                writer.Write($"TABLESAMPLE {node.TableSamplingRate}");
            }
        }

        private static void DeparseBindTableNode(ScriptWriter writer, BindTableNode node)
        {
            DeparseTableName(writer, node.Name);

            if (!string.IsNullOrEmpty(node.Alias))
            {
                writer.WriteSpace();
                writer.Write(node.Alias);
            }
        }

        private static void DeparseJoinTableNode(ScriptWriter writer, JoinTableNode node)
        {
            DeparseTableNode(writer, node.LHS);
            writer.WriteSpace();

            if (node.OnNode == null)
            {
                writer.Write(", ");
                DeparseTableNode(writer, node.RHS);
            }
            else
            {
                writer.Write(node.Type.ToSql());
                writer.Write(" JOIN ");
                DeparseTableNode(writer, node.RHS);
                writer.Write(" ON (");
                DeparseParseNode(writer, node.OnNode);
                writer.Write(')');
            }
        }

        private static void DeparseDerivedTableNode(ScriptWriter writer, DerivedTableNode node)
        {
            writer.Write('(');
            DeparseSelectStatement(writer, node.Select);
            writer.Write(')');

            if (!string.IsNullOrEmpty(node.Alias))
            {
                writer.WriteSpace();
                writer.Write(node.Alias);
                WriteAlias(writer, node.Alias);
            }
        }
        #endregion

        private static void DeparseCursorName(ScriptWriter writer, CursorName node)
        {
            WriteIdentifier(writer, node.Name, node.IsCaseSensitive);
        }

        private static void DeparseColumnName(ScriptWriter writer, ColumnName node)
        {
            var familyName = node.FamilyNode?.Name;
            var columnName = node.ColumnNode?.Name;

            writer.Write(PhoenixSchemaUtility.GetQualifiedTableName(familyName, columnName));
        }

        private static void DeparseFunctionArgument(ScriptWriter writer, FunctionArgument node)
        {
            throw new NotSupportedException();
        }

        private static void DeparseAliasedNode(ScriptWriter writer, AliasedNode node)
        {
            DeparseParseNode(writer, node.Node);

            if (!string.IsNullOrEmpty(node.Alias))
            {
                writer.WriteSpace();
                WriteIdentifier(writer, node.Alias, node.IsCaseSensitve);
            }
        }

        private static void DeparseTableName(ScriptWriter writer, TableName tableName)
        {
            if (!string.IsNullOrEmpty(tableName.SchemaName))
            {
                WriteIdentifier(writer, tableName.SchemaName, tableName.IsSchemaNameCaseSensitive);
                writer.Write('.');
            }

            WriteIdentifier(writer, tableName.TableName_, tableName.IsTableNameCaseSensitive);
        }

        #region IParseNode
        private static void DeparseParseNode(ScriptWriter writer, P_ParseNode node)
        {
            DeparseParseNode(writer, node.Message);
        }

        private static void DeparseParseNode(ScriptWriter writer, IParseNode node)
        {
            switch (node.Unwrap())
            {
                case INamedParseNode namedParseNode:
                    DeparseNamedParseNode(writer, namedParseNode);
                    break;

                case IComparisonParseNode comparisonParseNode:
                    DeparseComparisonParseNode(writer, comparisonParseNode);
                    break;

                case IArrayAllAnyComparisonNode arrayAllAnyComparisonNode:
                    DeparseArrayAllAnyComparisonNode(writer, arrayAllAnyComparisonNode);
                    break;

                case StringConcatParseNode stringConcatParseNode:
                    DeparseStringConcatParseNode(writer, stringConcatParseNode);
                    break;

                case FunctionParseNode functionParseNode:
                    DeparseFunctionParseNode(writer, functionParseNode);
                    break;

                case AggregateFunctionParseNode aggregateFunctionParseNode:
                    DeparseAggregateFunctionParseNode(writer, aggregateFunctionParseNode);
                    break;

                case AggregateFunctionWithinGroupParseNode aggregateFunctionWithinGroupParseNode:
                    DeparseAggregateFunctionWithinGroupParseNode(writer, aggregateFunctionWithinGroupParseNode);
                    break;

                case SumAggregateParseNode sumAggregateParseNode:
                    DeparseSumAggregateParseNode(writer, sumAggregateParseNode);
                    break;

                case LastValuesAggregateParseNode lastValuesAggregateParseNode:
                    DeparseLastValuesAggregateParseNode(writer, lastValuesAggregateParseNode);
                    break;

                case FirstValuesAggregateParseNode firstValuesAggregateParseNode:
                    DeparseFirstValuesAggregateParseNode(writer, firstValuesAggregateParseNode);
                    break;

                case NthValueAggregateParseNode nthValueAggregateParseNode:
                    DeparseNthValueAggregateParseNode(writer, nthValueAggregateParseNode);
                    break;

                case MinAggregateParseNode minAggregateParseNode:
                    DeparseMinAggregateParseNode(writer, minAggregateParseNode);
                    break;

                case MaxAggregateParseNode maxAggregateParseNode:
                    DeparseMaxAggregateParseNode(writer, maxAggregateParseNode);
                    break;

                case LastValueAggregateParseNode lastValueAggregateParseNode:
                    DeparseLastValueAggregateParseNode(writer, lastValueAggregateParseNode);
                    break;

                case DistinctCountHyperLogLogAggregateParseNode distinctCountHyperLogLogAggregateParseNode:
                    DeparseDistinctCountHyperLogLogAggregateParseNode(writer, distinctCountHyperLogLogAggregateParseNode);
                    break;

                case FirstValueAggregateParseNode firstValueAggregateParseNode:
                    DeparseFirstValueAggregateParseNode(writer, firstValueAggregateParseNode);
                    break;

                case DistinctCountParseNode distinctCountParseNode:
                    DeparseDistinctCountParseNode(writer, distinctCountParseNode);
                    break;

                case AvgAggregateParseNode avgAggregateParseNode:
                    DeparseAvgAggregateParseNode(writer, avgAggregateParseNode);
                    break;

                case ToTimeParseNode toTimeParseNode:
                    DeparseToTimeParseNode(writer, toTimeParseNode);
                    break;

                case CurrentTimeParseNode currentTimeParseNode:
                    DeparseCurrentTimeParseNode(writer, currentTimeParseNode);
                    break;

                case ToCharParseNode toCharParseNode:
                    DeparseToCharParseNode(writer, toCharParseNode);
                    break;

                case RegexpSplitParseNode regexpSplitParseNode:
                    DeparseRegexpSplitParseNode(writer, regexpSplitParseNode);
                    break;

                case ToTimestampParseNode toTimestampParseNode:
                    DeparseToTimestampParseNode(writer, toTimestampParseNode);
                    break;

                case CurrentDateParseNode currentDateParseNode:
                    DeparseCurrentDateParseNode(writer, currentDateParseNode);
                    break;

                case RegexpReplaceParseNode regexpReplaceParseNode:
                    DeparseRegexpReplaceParseNode(writer, regexpReplaceParseNode);
                    break;

                case ToNumberParseNode toNumberParseNode:
                    DeparseToNumberParseNode(writer, toNumberParseNode);
                    break;

                case ArrayModifierParseNode arrayModifierParseNode:
                    DeparseArrayModifierParseNode(writer, arrayModifierParseNode);
                    break;

                case RegexpSubstrParseNode regexpSubstrParseNode:
                    DeparseRegexpSubstrParseNode(writer, regexpSubstrParseNode);
                    break;

                case FloorParseNode floorParseNode:
                    DeparseFloorParseNode(writer, floorParseNode);
                    break;

                case UDFParseNode uDFParseNode:
                    DeparseUDFParseNode(writer, uDFParseNode);
                    break;

                case ToDateParseNode toDateParseNode:
                    DeparseToDateParseNode(writer, toDateParseNode);
                    break;

                case RoundParseNode roundParseNode:
                    DeparseRoundParseNode(writer, roundParseNode);
                    break;

                case CeilParseNode ceilParseNode:
                    DeparseCeilParseNode(writer, ceilParseNode);
                    break;

                case MultiplyParseNode multiplyParseNode:
                    DeparseMultiplyParseNode(writer, multiplyParseNode);
                    break;

                case AddParseNode addParseNode:
                    DeparseAddParseNode(writer, addParseNode);
                    break;

                case SubtractParseNode subtractParseNode:
                    DeparseSubtractParseNode(writer, subtractParseNode);
                    break;

                case ModulusParseNode modulusParseNode:
                    DeparseModulusParseNode(writer, modulusParseNode);
                    break;

                case DivideParseNode divideParseNode:
                    DeparseDivideParseNode(writer, divideParseNode);
                    break;

                case InParseNode inParseNode:
                    DeparseInParseNode(writer, inParseNode);
                    break;

                case LikeParseNode likeParseNode:
                    DeparseLikeParseNode(writer, likeParseNode);
                    break;

                case OrParseNode orParseNode:
                    DeparseOrParseNode(writer, orParseNode);
                    break;

                case ArrayElemRefNode arrayElemRefNode:
                    DeparseArrayElemRefNode(writer, arrayElemRefNode);
                    break;

                case InListParseNode inListParseNode:
                    DeparseInListParseNode(writer, inListParseNode);
                    break;

                case ExistsParseNode existsParseNode:
                    DeparseExistsParseNode(writer, existsParseNode);
                    break;

                case NotParseNode notParseNode:
                    DeparseNotParseNode(writer, notParseNode);
                    break;

                case IsNullParseNode isNullParseNode:
                    DeparseIsNullParseNode(writer, isNullParseNode);
                    break;

                case CastParseNode castParseNode:
                    DeparseCastParseNode(writer, castParseNode);
                    break;

                case RowValueConstructorParseNode rowValueConstructorParseNode:
                    DeparseRowValueConstructorParseNode(writer, rowValueConstructorParseNode);
                    break;

                case ArrayConstructorNode arrayConstructorNode:
                    DeparseArrayConstructorNode(writer, arrayConstructorNode);
                    break;

                case AndParseNode andParseNode:
                    DeparseAndParseNode(writer, andParseNode);
                    break;

                case CaseParseNode caseParseNode:
                    DeparseCaseParseNode(writer, caseParseNode);
                    break;

                case BetweenParseNode betweenParseNode:
                    DeparseBetweenParseNode(writer, betweenParseNode);
                    break;

                case SubqueryParseNode subqueryParseNode:
                    DeparseSubqueryParseNode(writer, subqueryParseNode);
                    break;

                case WildcardParseNode wildcardParseNode:
                    DeparseWildcardParseNode(writer, wildcardParseNode);
                    break;

                case SequenceValueParseNode sequenceValueParseNode:
                    DeparseSequenceValueParseNode(writer, sequenceValueParseNode);
                    break;

                case LiteralParseNode literalParseNode:
                    DeparseLiteralParseNode(writer, literalParseNode);
                    break;

                default:
                    throw new PhoenixSqlDeparserException(node);
            }
        }

        private static void DeparseStringConcatParseNode(ScriptWriter writer, StringConcatParseNode node)
        {
            writer.Write('(');
            writer.WriteJoin(" || ", node.Children, DeparseParseNode);
            writer.Write(')');
        }

        private static void DeparseFunctionParseNode(ScriptWriter writer, P_FunctionParseNode node)
        {
            DeparseFunctionParseNode(writer, node.Message);
        }

        private static void DeparseFunctionParseNode(ScriptWriter writer, IFunctionParseNode node)
        {
            writer.Write(node.Name);
            writer.Write('(');
            writer.WriteJoin(", ", node.Children, DeparseParseNode);
            writer.Write(')');
        }

        private static void DeparseAggregateFunctionParseNode(ScriptWriter writer, AggregateFunctionParseNode node)
        {
            DeparseFunctionParseNode(writer, node);
        }

        private static void DeparseAggregateFunctionWithinGroupParseNode(ScriptWriter writer, AggregateFunctionWithinGroupParseNode node)
        {
            writer.Write(node.Name);
            writer.Write('(');
            writer.WriteJoin(", ", node.Children.Skip(2), DeparseParseNode);
            writer.Write(')');

            bool asc = node.Children[1].UnwrapAs<LiteralParseNode>().IsTrue();

            writer.Write(" WITHIN GROUP (ORDER BY ");
            DeparseParseNode(writer, node.Children[0]);
            writer.Write(asc ? "ASC" : "DESC");
            writer.Write(')');
        }

        private static void DeparseSumAggregateParseNode(ScriptWriter writer, SumAggregateParseNode node)
        {
            DeparseFunctionParseNode(writer, node);
        }

        private static void DeparseLastValuesAggregateParseNode(ScriptWriter writer, LastValuesAggregateParseNode node)
        {
            DeparseFunctionParseNode(writer, node);
        }

        private static void DeparseFirstValuesAggregateParseNode(ScriptWriter writer, FirstValuesAggregateParseNode node)
        {
            DeparseFunctionParseNode(writer, node);
        }

        private static void DeparseNthValueAggregateParseNode(ScriptWriter writer, NthValueAggregateParseNode node)
        {
            DeparseFunctionParseNode(writer, node);
        }

        private static void DeparseMinAggregateParseNode(ScriptWriter writer, MinAggregateParseNode node)
        {
            DeparseFunctionParseNode(writer, node);
        }

        private static void DeparseMaxAggregateParseNode(ScriptWriter writer, MaxAggregateParseNode node)
        {
            DeparseFunctionParseNode(writer, node);
        }

        private static void DeparseLastValueAggregateParseNode(ScriptWriter writer, LastValueAggregateParseNode node)
        {
            DeparseFunctionParseNode(writer, node);
        }

        private static void DeparseDistinctCountHyperLogLogAggregateParseNode(ScriptWriter writer, DistinctCountHyperLogLogAggregateParseNode node)
        {
            DeparseFunctionParseNode(writer, node);
        }

        private static void DeparseFirstValueAggregateParseNode(ScriptWriter writer, FirstValueAggregateParseNode node)
        {
            DeparseFunctionParseNode(writer, node);
        }

        private static void DeparseDistinctCountParseNode(ScriptWriter writer, DistinctCountParseNode node)
        {
            writer.Write("COUNT(DISTINCT ");
            writer.WriteJoin(", ", node.Children, DeparseParseNode);
            writer.Write(')');
        }

        private static void DeparseAvgAggregateParseNode(ScriptWriter writer, AvgAggregateParseNode node)
        {
            DeparseFunctionParseNode(writer, node);
        }

        private static void DeparseToTimeParseNode(ScriptWriter writer, ToTimeParseNode node)
        {
            DeparseFunctionParseNode(writer, node);
        }

        private static void DeparseCurrentTimeParseNode(ScriptWriter writer, CurrentTimeParseNode node)
        {
            DeparseFunctionParseNode(writer, node);
        }

        private static void DeparseToCharParseNode(ScriptWriter writer, ToCharParseNode node)
        {
            DeparseFunctionParseNode(writer, node);
        }

        private static void DeparseRegexpSplitParseNode(ScriptWriter writer, RegexpSplitParseNode node)
        {
            DeparseFunctionParseNode(writer, node);
        }

        private static void DeparseToTimestampParseNode(ScriptWriter writer, ToTimestampParseNode node)
        {
            DeparseFunctionParseNode(writer, node);
        }

        private static void DeparseCurrentDateParseNode(ScriptWriter writer, CurrentDateParseNode node)
        {
            DeparseFunctionParseNode(writer, node);
        }

        private static void DeparseRegexpReplaceParseNode(ScriptWriter writer, RegexpReplaceParseNode node)
        {
            DeparseFunctionParseNode(writer, node);
        }

        private static void DeparseToNumberParseNode(ScriptWriter writer, ToNumberParseNode node)
        {
            DeparseFunctionParseNode(writer, node);
        }

        private static void DeparseArrayModifierParseNode(ScriptWriter writer, ArrayModifierParseNode node)
        {
            DeparseFunctionParseNode(writer, node);
        }

        private static void DeparseRegexpSubstrParseNode(ScriptWriter writer, RegexpSubstrParseNode node)
        {
            DeparseFunctionParseNode(writer, node);
        }

        private static void DeparseFloorParseNode(ScriptWriter writer, FloorParseNode node)
        {
            DeparseFunctionParseNode(writer, node);
        }

        private static void DeparseUDFParseNode(ScriptWriter writer, UDFParseNode node)
        {
            DeparseFunctionParseNode(writer, node);
        }

        private static void DeparseToDateParseNode(ScriptWriter writer, ToDateParseNode node)
        {
            DeparseFunctionParseNode(writer, node);
        }

        private static void DeparseRoundParseNode(ScriptWriter writer, RoundParseNode node)
        {
            DeparseFunctionParseNode(writer, node);
        }

        private static void DeparseCeilParseNode(ScriptWriter writer, CeilParseNode node)
        {
            DeparseFunctionParseNode(writer, node);
        }

        private static void DeparseMultiplyParseNode(ScriptWriter writer, MultiplyParseNode node)
        {
            writer.Write('(');
            writer.WriteJoin(" * ", node.Children, DeparseParseNode);
            writer.Write(')');
        }

        private static void DeparseAddParseNode(ScriptWriter writer, AddParseNode node)
        {
            writer.Write('(');
            writer.WriteJoin(" + ", node.Children, DeparseParseNode);
            writer.Write(')');
        }

        private static void DeparseSubtractParseNode(ScriptWriter writer, SubtractParseNode node)
        {
            writer.Write('(');
            writer.WriteJoin(" - ", node.Children, DeparseParseNode);
            writer.Write(')');
        }

        private static void DeparseModulusParseNode(ScriptWriter writer, ModulusParseNode node)
        {
            writer.Write('(');
            writer.WriteJoin(" % ", node.Children, DeparseParseNode);
            writer.Write(')');
        }

        private static void DeparseDivideParseNode(ScriptWriter writer, DivideParseNode node)
        {
            writer.Write('(');
            writer.WriteJoin(" / ", node.Children, DeparseParseNode);
            writer.Write(')');
        }

        private static void DeparseComparisonParseNode(ScriptWriter writer, P_ComparisonParseNode node)
        {
            DeparseComparisonParseNode(writer, node.Message);
        }

        private static void DeparseComparisonParseNode(ScriptWriter writer, IComparisonParseNode node)
        {
            DeparseParseNode(writer, node.Children[0]);
            writer.Write($" {node.FilterOp.ToSql()} ");
            DeparseParseNode(writer, node.Children[1]);
        }

        private static void DeparseInParseNode(ScriptWriter writer, InParseNode node)
        {
            DeparseParseNode(writer, node.Children[0]);

            if (node.IsNegate)
            {
                writer.WriteSpace();
                writer.Write("NOT");
            }

            writer.WriteSpace();
            writer.Write("IN (");
            DeparseParseNode(writer, node.Children[1]);
            writer.Write(')');
        }

        private static void DeparseLikeParseNode(ScriptWriter writer, LikeParseNode node)
        {
            if (node.IsNegate)
                writer.Write("NOT ");

            var likeOp = node.LikeType == LikeType.CaseSensitive ? "LIKE" : "ILIKE";

            DeparseParseNode(writer, node.Children[0]);
            writer.Write($" {likeOp} ");
            DeparseParseNode(writer, node.Children[1]);
        }

        private static void DeparseOrParseNode(ScriptWriter writer, OrParseNode node)
        {
            writer.Write('(');
            writer.WriteJoin(" OR ", node.Children, DeparseParseNode);
            writer.Write(')');
        }

        private static void DeparseArrayAllAnyComparisonNode(ScriptWriter writer, P_ArrayAllAnyComparisonNode node)
        {
            DeparseArrayAllAnyComparisonNode(writer, node.Message);
        }

        private static void DeparseArrayAllAnyComparisonNode(ScriptWriter writer, IArrayAllAnyComparisonNode node)
        {
            var rhs = node.Children[0];
            var comp = (IComparisonParseNode)node.Children[1];
            var lhs = comp.LHS;

            DeparseParseNode(writer, lhs);
            writer.Write($" {comp.FilterOp.ToSql()} ");
            writer.Write(node.Type);
            writer.Write('(');
            DeparseParseNode(writer, rhs);
            writer.Write(')');
        }

        private static void DeparseArrayElemRefNode(ScriptWriter writer, ArrayElemRefNode node)
        {
            DeparseParseNode(writer, node.Children[0]);
            writer.Write('[');
            DeparseParseNode(writer, node.Children[1]);
            writer.Write(']');
        }

        private static void DeparseInListParseNode(ScriptWriter writer, InListParseNode node)
        {
            DeparseParseNode(writer, node.Children[0]);
            writer.WriteSpace();

            if (node.IsNegate)
                writer.Write("NOT ");

            writer.Write("IN");
            writer.Write('(');
            writer.WriteJoin(", ", node.Children, DeparseParseNode);
            writer.Write(')');
        }

        private static void DeparseExistsParseNode(ScriptWriter writer, ExistsParseNode node)
        {
            if (node.IsNegate)
                writer.Write("NOT ");

            writer.Write("EXISTS ");

            DeparseParseNode(writer, node.Children[0]);
        }

        private static void DeparseNotParseNode(ScriptWriter writer, NotParseNode node)
        {
            writer.Write("NOT ");
            DeparseParseNode(writer, node.Children[0]);
        }

        private static void DeparseIsNullParseNode(ScriptWriter writer, IsNullParseNode node)
        {
            DeparseParseNode(writer, node.Children[0]);
            writer.Write(" IS");

            if (node.IsNegate)
                writer.Write(" NOT");

            writer.Write(" NULL");
        }

        private static void DeparseCastParseNode(ScriptWriter writer, CastParseNode node)
        {
            bool isArray = node.DataType.IsArray();
            var dataType = node.DataType;

            if (isArray)
                dataType = dataType.GetElementType();

            writer.Write("CAST(");
            DeparseParseNode(writer, node.Children[0]);
            writer.Write(" AS ");

            writer.Write(dataType.ToSqlTypeName());

            WriteTypeSize(writer, node.MaxLength, node.Scale);

            if (isArray)
            {
                writer.Write(' ');
                writer.Write("ARRAY");
            }

            writer.Write(')');
        }

        private static void DeparseRowValueConstructorParseNode(ScriptWriter writer, RowValueConstructorParseNode node)
        {
            writer.Write('(');
            writer.WriteJoin(", ", node.Children, DeparseParseNode);
            writer.Write(')');
        }

        private static void DeparseArrayConstructorNode(ScriptWriter writer, ArrayConstructorNode node)
        {
            writer.Write("ARRAY[");
            writer.WriteJoin(", ", node.Children, DeparseParseNode);
            writer.Write(']');
        }

        private static void DeparseAndParseNode(ScriptWriter writer, AndParseNode node)
        {
            writer.Write('(');
            writer.WriteJoin(" AND ", node.Children, DeparseParseNode);
            writer.Write(')');
        }

        private static void DeparseCaseParseNode(ScriptWriter writer, CaseParseNode node)
        {
            writer.Write("CASE ");

            for (int i = 0; i < node.Children.Count - 1; i += 2)
            {
                writer.Write("WHEN ");
                DeparseParseNode(writer, node.Children[i + 1]);
                writer.Write(" THEN ");
                DeparseParseNode(writer, node.Children[i]);
            }

            if (node.Children.Count % 2 != 0)
            {
                writer.Write(" ELSE ");
                DeparseParseNode(writer, node.Children[^1]);
            }

            writer.Write(" END");
        }

        private static void DeparseBetweenParseNode(ScriptWriter writer, BetweenParseNode node)
        {
            DeparseParseNode(writer, node.Children[0]);

            if (node.IsNegate)
                writer.Write(" NOT");

            writer.Write(" BETWEEN ");
            DeparseParseNode(writer, node.Children[1]);
            writer.Write(" AND ");
            DeparseParseNode(writer, node.Children[2]);
        }

        private static void DeparseSubqueryParseNode(ScriptWriter writer, SubqueryParseNode node)
        {
            writer.Write('(');
            DeparseSelectStatement(writer, node.SelectNode);
            writer.Write(')');
        }

        private static void DeparseWildcardParseNode(ScriptWriter writer, WildcardParseNode node)
        {
            writer.Write("*");
        }

        private static void DeparseSequenceValueParseNode(ScriptWriter writer, SequenceValueParseNode node)
        {
            writer.Write(node.Op == Op.CurrentValue ? "CURRENT" : "NEXT");
            writer.Write(" VALUE FORE ");
            DeparseTableName(writer, node.TableName);
        }

        private static void DeparseLiteralParseNode(ScriptWriter writer, LiteralParseNode node)
        {
            if (node.IsNull())
            {
                writer.Write("NULL");
            }
            else
            {
                var isCoercible =
                    node.Type == PDataType.Timestamp ||
                    node.Type == PDataType.Time ||
                    node.Type == PDataType.Date ||
                    node.Type == PDataType.UnsignedTimestamp ||
                    node.Type == PDataType.UnsignedTime ||
                    node.Type == PDataType.UnsignedDate;

                if (isCoercible)
                {
                    writer.Write(node.Type.ToSqlTypeName());
                    writer.Write(' ');
                }

                writer.Write(node.Value);
            }
        }
        #endregion

        #region INamedParseNode
        private static void DeparseNamedParseNode(ScriptWriter writer, P_NamedParseNode node)
        {
            DeparseNamedParseNode(writer, node.Message);
        }

        private static void DeparseNamedParseNode(ScriptWriter writer, INamedParseNode node)
        {
            switch (node.Unwrap())
            {
                case FamilyWildcardParseNode familyWildcardParseNode:
                    DeparseFamilyWildcardParseNode(writer, familyWildcardParseNode);
                    break;

                case BindParseNode bindParseNode:
                    DeparseBindParseNode(writer, bindParseNode);
                    break;

                case ColumnParseNode columnParseNode:
                    DeparseColumnParseNode(writer, columnParseNode);
                    break;

                case TableWildcardParseNode tableWildcardParseNode:
                    DeparseTableWildcardParseNode(writer, tableWildcardParseNode);
                    break;

                default:
                    throw new PhoenixSqlDeparserException(node);
            }
        }

        private static void DeparseFamilyWildcardParseNode(ScriptWriter writer, FamilyWildcardParseNode node)
        {
            WriteIdentifier(writer, node.Name, node.IsCaseSensitive);
            writer.Write(".*");
        }

        private static void DeparseBindParseNode(ScriptWriter writer, BindParseNode node)
        {
            writer.Write($":{node.Index}");
        }

        private static void DeparseColumnParseNode(ScriptWriter writer, ColumnParseNode node)
        {
            if (!string.IsNullOrEmpty(node.TableName))
            {
                WriteIdentifier(writer, node.TableName, node.IsTableNameCaseSensitive);
                writer.Write('.');
            }

            WriteIdentifier(writer, node.Name, node.IsCaseSensitive);
        }

        private static void DeparseTableWildcardParseNode(ScriptWriter writer, TableWildcardParseNode node)
        {
            WriteIdentifier(writer, node.Name, node.IsCaseSensitive);
            writer.Write(".*");
        }
        #endregion

        private static void WriteIdentifier(ScriptWriter writer, string value, bool caseSensitive)
        {
            if (caseSensitive)
                writer.Write('"');

            writer.Write(value);

            if (caseSensitive)
                writer.Write('"');
        }

        private static void WriteAlias(ScriptWriter writer, string alias)
        {
            WriteIdentifier(writer, alias, !_namePattern.IsMatch(alias));
        }

        // (2)
        // (3,4)
        private static void WriteTypeSize(ScriptWriter writer, int maxLength, int scale)
        {
            if (maxLength == 0)
                return;

            writer.Write('(');
            writer.Write(maxLength);

            if (scale != 0)
            {
                writer.Write(",");
                writer.Write(scale);
            }

            writer.Write(')');
        }
    }
}
