package com.chequer.phoenixsql.util;

import com.chequer.phoenixsql.proto.Nodes;
import com.google.gson.Gson;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.security.access.Permission;
import org.apache.hadoop.hbase.util.Pair;
import org.apache.phoenix.expression.LiteralExpression;
import org.apache.phoenix.jdbc.PhoenixStatement;
import org.apache.phoenix.parse.*;
import org.apache.phoenix.schema.*;
import org.apache.phoenix.schema.types.*;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class NodeConverter {
    private static final Gson gson = new Gson();

    public static Nodes.AlterSessionStatement.Builder convert(AlterSessionStatement value) {
        if (value == null) return null;

        var builder = Nodes.AlterSessionStatement.newBuilder();

        builder.setBindCount(value.getBindCount());
        var v0 = value.getOperation();
        if (v0 != null) builder.setOperation(convert(v0));

        return builder;
    }

    public static Nodes.IndexType convert(PTable.IndexType value) {
        if (value == PTable.IndexType.GLOBAL) {
            return Nodes.IndexType.GLOBAL;
        }

        return Nodes.IndexType.LOCAL;
    }

    public static Nodes.DMLStatement.Builder convertDefault(DMLStatement value) {
        if (value == null) return null;

        var builder = Nodes.DMLStatement.newBuilder();

        builder.setBindCount(value.getBindCount());
        var v0 = value.getOperation();
        if (v0 != null) builder.setOperation(convert(v0));
        var v1 = value.getTable();
        if (v1 != null) builder.setTable(convert(v1));

        return builder;
    }

    public static Nodes.P_DMLStatement.Builder convert(DMLStatement value) {
        if (value == null) return null;

        var builder = Nodes.P_DMLStatement.newBuilder();

        if (value instanceof DeleteStatement) {
            builder.setDeleteStatement(convert((DeleteStatement) value));
        } else if (value instanceof UpsertStatement) {
            builder.setUpsertStatement(convert((UpsertStatement) value));
        } else if (value instanceof DMLStatement) {
            builder.setDMLStatement(convertDefault((DMLStatement) value));
        }

        return builder;
    }

    public static Nodes.RowValueConstructorParseNode.Builder convert(RowValueConstructorParseNode value) {
        if (value == null) return null;

        var builder = Nodes.RowValueConstructorParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());

        return builder;
    }

    public static Nodes.UDFParseNode.Builder convert(UDFParseNode value) {
        if (value == null) return null;

        var builder = Nodes.UDFParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getName();
        if (v1 != null) builder.setName(v1);
        builder.setIsAggregate(value.isAggregate());

        return builder;
    }

    public static Nodes.UpdateStatisticsStatement.Builder convert(UpdateStatisticsStatement value) {
        if (value == null) return null;

        var builder = Nodes.UpdateStatisticsStatement.newBuilder();

        builder.setBindCount(value.getBindCount());
        var v0 = value.getOperation();
        if (v0 != null) builder.setOperation(convert(v0));
        var v1 = value.getTable();
        if (v1 != null) builder.setTable(convert(v1));
        builder.setUpdateAll(value.updateAll());
        builder.setUpdateColumns(value.updateColumns());
        builder.setUpdateIndex(value.updateIndex());

        return builder;
    }

    public static Nodes.AggregateFunctionParseNode.Builder convertDefault(AggregateFunctionParseNode value) {
        if (value == null) return null;

        var builder = Nodes.AggregateFunctionParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getName();
        if (v1 != null) builder.setName(v1);
        builder.setIsAggregate(value.isAggregate());

        return builder;
    }

    public static Nodes.P_AggregateFunctionParseNode.Builder convert(AggregateFunctionParseNode value) {
        if (value == null) return null;

        var builder = Nodes.P_AggregateFunctionParseNode.newBuilder();

        if (value instanceof FirstValuesAggregateParseNode) {
            builder.setFirstValuesAggregateParseNode(convert((FirstValuesAggregateParseNode) value));
        } else if (value instanceof NthValueAggregateParseNode) {
            builder.setNthValueAggregateParseNode(convert((NthValueAggregateParseNode) value));
        } else if (value instanceof MinAggregateParseNode) {
            builder.setMinAggregateParseNode(convert((MinAggregateParseNode) value));
        } else if (value instanceof DistinctCountParseNode) {
            builder.setDistinctCountParseNode(convert((DistinctCountParseNode) value));
        } else if (value instanceof LastValuesAggregateParseNode) {
            builder.setLastValuesAggregateParseNode(convert((LastValuesAggregateParseNode) value));
        } else if (value instanceof SumAggregateParseNode) {
            builder.setSumAggregateParseNode(convert((SumAggregateParseNode) value));
        } else if (value instanceof FirstValueAggregateParseNode) {
            builder.setFirstValueAggregateParseNode(convert((FirstValueAggregateParseNode) value));
        } else if (value instanceof LastValueAggregateParseNode) {
            builder.setLastValueAggregateParseNode(convert((LastValueAggregateParseNode) value));
        } else if (value instanceof DistinctCountHyperLogLogAggregateParseNode) {
            builder.setDistinctCountHyperLogLogAggregateParseNode(convert((DistinctCountHyperLogLogAggregateParseNode) value));
        } else if (value instanceof MaxAggregateParseNode) {
            builder.setMaxAggregateParseNode(convert((MaxAggregateParseNode) value));
        } else if (value instanceof AggregateFunctionWithinGroupParseNode) {
            builder.setAggregateFunctionWithinGroupParseNode(convert((AggregateFunctionWithinGroupParseNode) value));
        } else if (value instanceof AvgAggregateParseNode) {
            builder.setAvgAggregateParseNode(convert((AvgAggregateParseNode) value));
        } else if (value instanceof AggregateFunctionParseNode) {
            builder.setAggregateFunctionParseNode(convertDefault((AggregateFunctionParseNode) value));
        }

        return builder;
    }

    public static Nodes.DropTableStatement.Builder convert(DropTableStatement value) {
        if (value == null) return null;

        var builder = Nodes.DropTableStatement.newBuilder();

        builder.setBindCount(value.getBindCount());
        var v0 = value.getOperation();
        if (v0 != null) builder.setOperation(convert(v0));
        builder.setCascade(value.cascade());
        var v1 = value.getTableName();
        if (v1 != null) builder.setTableName(convert(v1));
        var v2 = value.getTableType();
        if (v2 != null) builder.setTableType(convert(v2));
        builder.setIfExists(value.ifExists());

        return builder;
    }

    public static Nodes.RegexpReplaceParseNode.Builder convert(RegexpReplaceParseNode value) {
        if (value == null) return null;

        var builder = Nodes.RegexpReplaceParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getName();
        if (v1 != null) builder.setName(v1);
        builder.setIsAggregate(value.isAggregate());

        return builder;
    }

    public static Nodes.JoinTableNode.Builder convert(JoinTableNode value) {
        if (value == null) return null;

        var builder = Nodes.JoinTableNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        var v1 = value.getLHS();
        if (v1 != null) builder.setLHS(convert(v1));
        var v2 = value.getOnNode();
        if (v2 != null) builder.setOnNode(convert(v2));
        var v3 = value.getRHS();
        if (v3 != null) builder.setRHS(convert(v3));
        var v4 = value.getType();
        if (v4 != null) builder.setType(convert(v4));
        builder.setIsSingleValueOnly(value.isSingleValueOnly());

        return builder;
    }

    public static Nodes.ColumnParseNode.Builder convert(ColumnParseNode value) {
        if (value == null) return null;

        var builder = Nodes.ColumnParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getName();
        if (v1 != null) builder.setName(v1);
        builder.setIsCaseSensitive(value.isCaseSensitive());
        var v2 = value.getFullName();
        if (v2 != null) builder.setFullName(v2);
        var v3 = value.getSchemaName();
        if (v3 != null) builder.setSchemaName(v3);
        var v4 = value.getTableName();
        if (v4 != null) builder.setTableName(v4);
        builder.setIsTableNameCaseSensitive(value.isTableNameCaseSensitive());

        return builder;
    }

    public static Nodes.FetchStatement.Builder convert(FetchStatement value) {
        if (value == null) return null;

        var builder = Nodes.FetchStatement.newBuilder();

        builder.setBindCount(value.getBindCount());
        var v0 = value.getOperation();
        if (v0 != null) builder.setOperation(convert(v0));
        var v1 = value.getCursorName();
        if (v1 != null) builder.setCursorName(convert(v1));
        builder.setFetchSize(value.getFetchSize());
        builder.setIsNext(value.isNext());

        return builder;
    }

    public static Nodes.Op convert(SequenceValueParseNode.Op value) {
        if (value == SequenceValueParseNode.Op.NEXT_VALUE) {
            return Nodes.Op.NEXT_VALUE;
        }

        return Nodes.Op.CURRENT_VALUE;
    }

    public static Nodes.BetweenParseNode.Builder convert(BetweenParseNode value) {
        if (value == null) return null;

        var builder = Nodes.BetweenParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        builder.setIsNegate(value.isNegate());

        return builder;
    }

    public static Nodes.P_CompoundParseNode.Builder convert(CompoundParseNode value) {
        if (value == null) return null;

        var builder = Nodes.P_CompoundParseNode.newBuilder();

        if (value instanceof RowValueConstructorParseNode) {
            builder.setRowValueConstructorParseNode(convert((RowValueConstructorParseNode) value));
        } else if (value instanceof UDFParseNode) {
            builder.setUDFParseNode(convert((UDFParseNode) value));
        } else if (value instanceof FirstValuesAggregateParseNode) {
            builder.setFirstValuesAggregateParseNode(convert((FirstValuesAggregateParseNode) value));
        } else if (value instanceof NthValueAggregateParseNode) {
            builder.setNthValueAggregateParseNode(convert((NthValueAggregateParseNode) value));
        } else if (value instanceof MinAggregateParseNode) {
            builder.setMinAggregateParseNode(convert((MinAggregateParseNode) value));
        } else if (value instanceof DistinctCountParseNode) {
            builder.setDistinctCountParseNode(convert((DistinctCountParseNode) value));
        } else if (value instanceof LastValuesAggregateParseNode) {
            builder.setLastValuesAggregateParseNode(convert((LastValuesAggregateParseNode) value));
        } else if (value instanceof SumAggregateParseNode) {
            builder.setSumAggregateParseNode(convert((SumAggregateParseNode) value));
        } else if (value instanceof FirstValueAggregateParseNode) {
            builder.setFirstValueAggregateParseNode(convert((FirstValueAggregateParseNode) value));
        } else if (value instanceof LastValueAggregateParseNode) {
            builder.setLastValueAggregateParseNode(convert((LastValueAggregateParseNode) value));
        } else if (value instanceof DistinctCountHyperLogLogAggregateParseNode) {
            builder.setDistinctCountHyperLogLogAggregateParseNode(convert((DistinctCountHyperLogLogAggregateParseNode) value));
        } else if (value instanceof MaxAggregateParseNode) {
            builder.setMaxAggregateParseNode(convert((MaxAggregateParseNode) value));
        } else if (value instanceof AggregateFunctionWithinGroupParseNode) {
            builder.setAggregateFunctionWithinGroupParseNode(convert((AggregateFunctionWithinGroupParseNode) value));
        } else if (value instanceof AvgAggregateParseNode) {
            builder.setAvgAggregateParseNode(convert((AvgAggregateParseNode) value));
        } else if (value instanceof RegexpReplaceParseNode) {
            builder.setRegexpReplaceParseNode(convert((RegexpReplaceParseNode) value));
        } else if (value instanceof ToTimeParseNode) {
            builder.setToTimeParseNode(convert((ToTimeParseNode) value));
        } else if (value instanceof CurrentTimeParseNode) {
            builder.setCurrentTimeParseNode(convert((CurrentTimeParseNode) value));
        } else if (value instanceof ToNumberParseNode) {
            builder.setToNumberParseNode(convert((ToNumberParseNode) value));
        } else if (value instanceof RoundParseNode) {
            builder.setRoundParseNode(convert((RoundParseNode) value));
        } else if (value instanceof ArrayModifierParseNode) {
            builder.setArrayModifierParseNode(convert((ArrayModifierParseNode) value));
        } else if (value instanceof RegexpSubstrParseNode) {
            builder.setRegexpSubstrParseNode(convert((RegexpSubstrParseNode) value));
        } else if (value instanceof FloorParseNode) {
            builder.setFloorParseNode(convert((FloorParseNode) value));
        } else if (value instanceof ToDateParseNode) {
            builder.setToDateParseNode(convert((ToDateParseNode) value));
        } else if (value instanceof RegexpSplitParseNode) {
            builder.setRegexpSplitParseNode(convert((RegexpSplitParseNode) value));
        } else if (value instanceof CurrentDateParseNode) {
            builder.setCurrentDateParseNode(convert((CurrentDateParseNode) value));
        } else if (value instanceof CeilParseNode) {
            builder.setCeilParseNode(convert((CeilParseNode) value));
        } else if (value instanceof ToTimestampParseNode) {
            builder.setToTimestampParseNode(convert((ToTimestampParseNode) value));
        } else if (value instanceof ToCharParseNode) {
            builder.setToCharParseNode(convert((ToCharParseNode) value));
        } else if (value instanceof BetweenParseNode) {
            builder.setBetweenParseNode(convert((BetweenParseNode) value));
        } else if (value instanceof ArrayAnyComparisonNode) {
            builder.setArrayAnyComparisonNode(convert((ArrayAnyComparisonNode) value));
        } else if (value instanceof ArrayAllComparisonNode) {
            builder.setArrayAllComparisonNode(convert((ArrayAllComparisonNode) value));
        } else if (value instanceof EqualParseNode) {
            builder.setEqualParseNode(convert((EqualParseNode) value));
        } else if (value instanceof LessThanParseNode) {
            builder.setLessThanParseNode(convert((LessThanParseNode) value));
        } else if (value instanceof GreaterThanParseNode) {
            builder.setGreaterThanParseNode(convert((GreaterThanParseNode) value));
        } else if (value instanceof NotEqualParseNode) {
            builder.setNotEqualParseNode(convert((NotEqualParseNode) value));
        } else if (value instanceof LessThanOrEqualParseNode) {
            builder.setLessThanOrEqualParseNode(convert((LessThanOrEqualParseNode) value));
        } else if (value instanceof GreaterThanOrEqualParseNode) {
            builder.setGreaterThanOrEqualParseNode(convert((GreaterThanOrEqualParseNode) value));
        } else if (value instanceof LikeParseNode) {
            builder.setLikeParseNode(convert((LikeParseNode) value));
        } else if (value instanceof InParseNode) {
            builder.setInParseNode(convert((InParseNode) value));
        } else if (value instanceof ArrayElemRefNode) {
            builder.setArrayElemRefNode(convert((ArrayElemRefNode) value));
        } else if (value instanceof NotParseNode) {
            builder.setNotParseNode(convert((NotParseNode) value));
        } else if (value instanceof IsNullParseNode) {
            builder.setIsNullParseNode(convert((IsNullParseNode) value));
        } else if (value instanceof CastParseNode) {
            builder.setCastParseNode(convert((CastParseNode) value));
        } else if (value instanceof ExistsParseNode) {
            builder.setExistsParseNode(convert((ExistsParseNode) value));
        } else if (value instanceof OrParseNode) {
            builder.setOrParseNode(convert((OrParseNode) value));
        } else if (value instanceof InListParseNode) {
            builder.setInListParseNode(convert((InListParseNode) value));
        } else if (value instanceof AndParseNode) {
            builder.setAndParseNode(convert((AndParseNode) value));
        } else if (value instanceof SubtractParseNode) {
            builder.setSubtractParseNode(convert((SubtractParseNode) value));
        } else if (value instanceof ModulusParseNode) {
            builder.setModulusParseNode(convert((ModulusParseNode) value));
        } else if (value instanceof MultiplyParseNode) {
            builder.setMultiplyParseNode(convert((MultiplyParseNode) value));
        } else if (value instanceof AddParseNode) {
            builder.setAddParseNode(convert((AddParseNode) value));
        } else if (value instanceof DivideParseNode) {
            builder.setDivideParseNode(convert((DivideParseNode) value));
        } else if (value instanceof ArrayConstructorNode) {
            builder.setArrayConstructorNode(convert((ArrayConstructorNode) value));
        } else if (value instanceof StringConcatParseNode) {
            builder.setStringConcatParseNode(convert((StringConcatParseNode) value));
        } else if (value instanceof CaseParseNode) {
            builder.setCaseParseNode(convert((CaseParseNode) value));
        } else if (value instanceof AggregateFunctionParseNode) {
            builder.setAggregateFunctionParseNode(convertDefault((AggregateFunctionParseNode) value));
        } else if (value instanceof FunctionParseNode) {
            builder.setFunctionParseNode(convertDefault((FunctionParseNode) value));
        }

        return builder;
    }

    public static Nodes.P_ArrayAllAnyComparisonNode.Builder convert(ArrayAllAnyComparisonNode value) {
        if (value == null) return null;

        var builder = Nodes.P_ArrayAllAnyComparisonNode.newBuilder();

        if (value instanceof ArrayAnyComparisonNode) {
            builder.setArrayAnyComparisonNode(convert((ArrayAnyComparisonNode) value));
        } else if (value instanceof ArrayAllComparisonNode) {
            builder.setArrayAllComparisonNode(convert((ArrayAllComparisonNode) value));
        }

        return builder;
    }

    public static Nodes.P_BinaryParseNode.Builder convert(BinaryParseNode value) {
        if (value == null) return null;

        var builder = Nodes.P_BinaryParseNode.newBuilder();

        if (value instanceof EqualParseNode) {
            builder.setEqualParseNode(convert((EqualParseNode) value));
        } else if (value instanceof LessThanParseNode) {
            builder.setLessThanParseNode(convert((LessThanParseNode) value));
        } else if (value instanceof GreaterThanParseNode) {
            builder.setGreaterThanParseNode(convert((GreaterThanParseNode) value));
        } else if (value instanceof NotEqualParseNode) {
            builder.setNotEqualParseNode(convert((NotEqualParseNode) value));
        } else if (value instanceof LessThanOrEqualParseNode) {
            builder.setLessThanOrEqualParseNode(convert((LessThanOrEqualParseNode) value));
        } else if (value instanceof GreaterThanOrEqualParseNode) {
            builder.setGreaterThanOrEqualParseNode(convert((GreaterThanOrEqualParseNode) value));
        } else if (value instanceof LikeParseNode) {
            builder.setLikeParseNode(convert((LikeParseNode) value));
        } else if (value instanceof InParseNode) {
            builder.setInParseNode(convert((InParseNode) value));
        }

        return builder;
    }

    public static Nodes.PTableKey.Builder convert(PTableKey value) {
        if (value == null) return null;

        var builder = Nodes.PTableKey.newBuilder();

        var v0 = value.getName();
        if (v0 != null) builder.setName(v0);
        var v1 = value.getTenantId();
        if (v1 != null) builder.setTenantId(convert(v1));

        return builder;
    }

    public static Nodes.ListJarsStatement.Builder convert(ListJarsStatement value) {
        if (value == null) return null;

        var builder = Nodes.ListJarsStatement.newBuilder();

        builder.setBindCount(value.getBindCount());
        var v0 = value.getOperation();
        if (v0 != null) builder.setOperation(convert(v0));

        return builder;
    }

    public static Nodes.DerivedTableNode.Builder convert(DerivedTableNode value) {
        if (value == null) return null;

        var builder = Nodes.DerivedTableNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        var v1 = value.getSelect();
        if (v1 != null) builder.setSelect(convert(v1));

        return builder;
    }

    public static Nodes.Hint convert(HintNode.Hint value) {
        if (value == HintNode.Hint.RANGE_SCAN) {
            return Nodes.Hint.RANGE_SCAN;
        } else if (value == HintNode.Hint.SKIP_SCAN) {
            return Nodes.Hint.SKIP_SCAN;
        } else if (value == HintNode.Hint.NO_CHILD_PARENT_JOIN_OPTIMIZATION) {
            return Nodes.Hint.NO_CHILD_PARENT_JOIN_OPTIMIZATION;
        } else if (value == HintNode.Hint.NO_INDEX) {
            return Nodes.Hint.NO_INDEX;
        } else if (value == HintNode.Hint.INDEX) {
            return Nodes.Hint.INDEX_1;
        } else if (value == HintNode.Hint.USE_DATA_OVER_INDEX_TABLE) {
            return Nodes.Hint.USE_DATA_OVER_INDEX_TABLE;
        } else if (value == HintNode.Hint.USE_INDEX_OVER_DATA_TABLE) {
            return Nodes.Hint.USE_INDEX_OVER_DATA_TABLE;
        } else if (value == HintNode.Hint.NO_CACHE) {
            return Nodes.Hint.NO_CACHE;
        } else if (value == HintNode.Hint.USE_SORT_MERGE_JOIN) {
            return Nodes.Hint.USE_SORT_MERGE_JOIN;
        } else if (value == HintNode.Hint.NO_STAR_JOIN) {
            return Nodes.Hint.NO_STAR_JOIN;
        } else if (value == HintNode.Hint.SEEK_TO_COLUMN) {
            return Nodes.Hint.SEEK_TO_COLUMN;
        } else if (value == HintNode.Hint.NO_SEEK_TO_COLUMN) {
            return Nodes.Hint.NO_SEEK_TO_COLUMN;
        } else if (value == HintNode.Hint.SMALL) {
            return Nodes.Hint.SMALL;
        } else if (value == HintNode.Hint.SERIAL) {
            return Nodes.Hint.SERIAL;
        }

        return Nodes.Hint.FORWARD_SCAN;
    }

    public static Nodes.CompareOp convert(CompareFilter.CompareOp value) {
        if (value == CompareFilter.CompareOp.LESS) {
            return Nodes.CompareOp.LESS;
        } else if (value == CompareFilter.CompareOp.LESS_OR_EQUAL) {
            return Nodes.CompareOp.LESS_OR_EQUAL;
        } else if (value == CompareFilter.CompareOp.EQUAL) {
            return Nodes.CompareOp.EQUAL;
        } else if (value == CompareFilter.CompareOp.NOT_EQUAL) {
            return Nodes.CompareOp.NOT_EQUAL;
        } else if (value == CompareFilter.CompareOp.GREATER_OR_EQUAL) {
            return Nodes.CompareOp.GREATER_OR_EQUAL;
        } else if (value == CompareFilter.CompareOp.GREATER) {
            return Nodes.CompareOp.GREATER;
        }

        return Nodes.CompareOp.NO_OP;
    }

    public static Nodes.I_FilterableStatement.Builder convert(FilterableStatement value) {
        if (value == null) return null;

        var builder = Nodes.I_FilterableStatement.newBuilder();

        if (value instanceof SelectStatement) {
            builder.setSelectStatement(convert((SelectStatement) value));
        }

        return builder;
    }

    public static Nodes.ToTimeParseNode.Builder convert(ToTimeParseNode value) {
        if (value == null) return null;

        var builder = Nodes.ToTimeParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getName();
        if (v1 != null) builder.setName(v1);
        builder.setIsAggregate(value.isAggregate());

        return builder;
    }

    public static Nodes.ArrayElemRefNode.Builder convert(ArrayElemRefNode value) {
        if (value == null) return null;

        var builder = Nodes.ArrayElemRefNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());

        return builder;
    }

    public static Nodes.PIndexState convert(PIndexState value) {
        if (value == PIndexState.BUILDING) {
            return Nodes.PIndexState.BUILDING;
        } else if (value == PIndexState.USABLE) {
            return Nodes.PIndexState.USABLE;
        } else if (value == PIndexState.UNUSABLE) {
            return Nodes.PIndexState.UNUSABLE;
        } else if (value == PIndexState.ACTIVE) {
            return Nodes.PIndexState.ACTIVE;
        } else if (value == PIndexState.INACTIVE) {
            return Nodes.PIndexState.INACTIVE;
        } else if (value == PIndexState.DISABLE) {
            return Nodes.PIndexState.DISABLE;
        } else if (value == PIndexState.REBUILD) {
            return Nodes.PIndexState.REBUILD;
        } else if (value == PIndexState.PENDING_ACTIVE) {
            return Nodes.PIndexState.PENDING_ACTIVE;
        }

        return Nodes.PIndexState.PENDING_DISABLE;
    }

    public static Nodes.FunctionParseNode.Builder convertDefault(FunctionParseNode value) {
        if (value == null) return null;

        var builder = Nodes.FunctionParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getName();
        if (v1 != null) builder.setName(v1);
        builder.setIsAggregate(value.isAggregate());

        return builder;
    }

    public static Nodes.P_FunctionParseNode.Builder convert(FunctionParseNode value) {
        if (value == null) return null;

        var builder = Nodes.P_FunctionParseNode.newBuilder();

        if (value instanceof UDFParseNode) {
            builder.setUDFParseNode(convert((UDFParseNode) value));
        } else if (value instanceof FirstValuesAggregateParseNode) {
            builder.setFirstValuesAggregateParseNode(convert((FirstValuesAggregateParseNode) value));
        } else if (value instanceof NthValueAggregateParseNode) {
            builder.setNthValueAggregateParseNode(convert((NthValueAggregateParseNode) value));
        } else if (value instanceof MinAggregateParseNode) {
            builder.setMinAggregateParseNode(convert((MinAggregateParseNode) value));
        } else if (value instanceof DistinctCountParseNode) {
            builder.setDistinctCountParseNode(convert((DistinctCountParseNode) value));
        } else if (value instanceof LastValuesAggregateParseNode) {
            builder.setLastValuesAggregateParseNode(convert((LastValuesAggregateParseNode) value));
        } else if (value instanceof SumAggregateParseNode) {
            builder.setSumAggregateParseNode(convert((SumAggregateParseNode) value));
        } else if (value instanceof FirstValueAggregateParseNode) {
            builder.setFirstValueAggregateParseNode(convert((FirstValueAggregateParseNode) value));
        } else if (value instanceof LastValueAggregateParseNode) {
            builder.setLastValueAggregateParseNode(convert((LastValueAggregateParseNode) value));
        } else if (value instanceof DistinctCountHyperLogLogAggregateParseNode) {
            builder.setDistinctCountHyperLogLogAggregateParseNode(convert((DistinctCountHyperLogLogAggregateParseNode) value));
        } else if (value instanceof MaxAggregateParseNode) {
            builder.setMaxAggregateParseNode(convert((MaxAggregateParseNode) value));
        } else if (value instanceof AggregateFunctionWithinGroupParseNode) {
            builder.setAggregateFunctionWithinGroupParseNode(convert((AggregateFunctionWithinGroupParseNode) value));
        } else if (value instanceof AvgAggregateParseNode) {
            builder.setAvgAggregateParseNode(convert((AvgAggregateParseNode) value));
        } else if (value instanceof RegexpReplaceParseNode) {
            builder.setRegexpReplaceParseNode(convert((RegexpReplaceParseNode) value));
        } else if (value instanceof ToTimeParseNode) {
            builder.setToTimeParseNode(convert((ToTimeParseNode) value));
        } else if (value instanceof CurrentTimeParseNode) {
            builder.setCurrentTimeParseNode(convert((CurrentTimeParseNode) value));
        } else if (value instanceof ToNumberParseNode) {
            builder.setToNumberParseNode(convert((ToNumberParseNode) value));
        } else if (value instanceof RoundParseNode) {
            builder.setRoundParseNode(convert((RoundParseNode) value));
        } else if (value instanceof ArrayModifierParseNode) {
            builder.setArrayModifierParseNode(convert((ArrayModifierParseNode) value));
        } else if (value instanceof RegexpSubstrParseNode) {
            builder.setRegexpSubstrParseNode(convert((RegexpSubstrParseNode) value));
        } else if (value instanceof FloorParseNode) {
            builder.setFloorParseNode(convert((FloorParseNode) value));
        } else if (value instanceof ToDateParseNode) {
            builder.setToDateParseNode(convert((ToDateParseNode) value));
        } else if (value instanceof RegexpSplitParseNode) {
            builder.setRegexpSplitParseNode(convert((RegexpSplitParseNode) value));
        } else if (value instanceof CurrentDateParseNode) {
            builder.setCurrentDateParseNode(convert((CurrentDateParseNode) value));
        } else if (value instanceof CeilParseNode) {
            builder.setCeilParseNode(convert((CeilParseNode) value));
        } else if (value instanceof ToTimestampParseNode) {
            builder.setToTimestampParseNode(convert((ToTimestampParseNode) value));
        } else if (value instanceof ToCharParseNode) {
            builder.setToCharParseNode(convert((ToCharParseNode) value));
        } else if (value instanceof AggregateFunctionParseNode) {
            builder.setAggregateFunctionParseNode(convertDefault((AggregateFunctionParseNode) value));
        } else if (value instanceof FunctionParseNode) {
            builder.setFunctionParseNode(convertDefault((FunctionParseNode) value));
        }

        return builder;
    }

    public static Nodes.OrderByNode.Builder convert(OrderByNode value) {
        if (value == null) return null;

        var builder = Nodes.OrderByNode.newBuilder();

        var v0 = value.getNode();
        if (v0 != null) builder.setNode(convert(v0));
        builder.setIsAscending(value.isAscending());
        builder.setIsNullsLast(value.isNullsLast());

        return builder;
    }

    public static Nodes.FirstValuesAggregateParseNode.Builder convert(FirstValuesAggregateParseNode value) {
        if (value == null) return null;

        var builder = Nodes.FirstValuesAggregateParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getName();
        if (v1 != null) builder.setName(v1);
        builder.setIsAggregate(value.isAggregate());

        return builder;
    }

    public static Nodes.CreateSequenceStatement.Builder convert(CreateSequenceStatement value) {
        if (value == null) return null;

        var builder = Nodes.CreateSequenceStatement.newBuilder();

        builder.setBindCount(value.getBindCount());
        var v0 = value.getOperation();
        if (v0 != null) builder.setOperation(convert(v0));
        var v1 = value.getCacheSize();
        if (v1 != null) builder.setCacheSize(convert(v1));
        builder.setCycle(value.getCycle());
        var v2 = value.getIncrementBy();
        if (v2 != null) builder.setIncrementBy(convert(v2));
        var v3 = value.getMaxValue();
        if (v3 != null) builder.setMaxValue(convert(v3));
        var v4 = value.getMinValue();
        if (v4 != null) builder.setMinValue(convert(v4));
        var v5 = value.getSequenceName();
        if (v5 != null) builder.setSequenceName(convert(v5));
        var v6 = value.getStartWith();
        if (v6 != null) builder.setStartWith(convert(v6));
        builder.setIfNotExists(value.ifNotExists());

        return builder;
    }

    public static Nodes.P_ComparisonParseNode.Builder convert(ComparisonParseNode value) {
        if (value == null) return null;

        var builder = Nodes.P_ComparisonParseNode.newBuilder();

        if (value instanceof EqualParseNode) {
            builder.setEqualParseNode(convert((EqualParseNode) value));
        } else if (value instanceof LessThanParseNode) {
            builder.setLessThanParseNode(convert((LessThanParseNode) value));
        } else if (value instanceof GreaterThanParseNode) {
            builder.setGreaterThanParseNode(convert((GreaterThanParseNode) value));
        } else if (value instanceof NotEqualParseNode) {
            builder.setNotEqualParseNode(convert((NotEqualParseNode) value));
        } else if (value instanceof LessThanOrEqualParseNode) {
            builder.setLessThanOrEqualParseNode(convert((LessThanOrEqualParseNode) value));
        } else if (value instanceof GreaterThanOrEqualParseNode) {
            builder.setGreaterThanOrEqualParseNode(convert((GreaterThanOrEqualParseNode) value));
        }

        return builder;
    }

    public static Nodes.SequenceValueParseNode.Builder convert(SequenceValueParseNode value) {
        if (value == null) return null;

        var builder = Nodes.SequenceValueParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getNumToAllocateNode();
        if (v1 != null) builder.setNumToAllocateNode(convert(v1));
        var v2 = value.getOp();
        if (v2 != null) builder.setOp(convert(v2));
        var v3 = value.getTableName();
        if (v3 != null) builder.setTableName(convert(v3));

        return builder;
    }

    public static Nodes.CurrentTimeParseNode.Builder convert(CurrentTimeParseNode value) {
        if (value == null) return null;

        var builder = Nodes.CurrentTimeParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getName();
        if (v1 != null) builder.setName(v1);
        builder.setIsAggregate(value.isAggregate());

        return builder;
    }

    public static Nodes.ToNumberParseNode.Builder convert(ToNumberParseNode value) {
        if (value == null) return null;

        var builder = Nodes.ToNumberParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getName();
        if (v1 != null) builder.setName(v1);
        builder.setIsAggregate(value.isAggregate());

        return builder;
    }

    public static Nodes.NotParseNode.Builder convert(NotParseNode value) {
        if (value == null) return null;

        var builder = Nodes.NotParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());

        return builder;
    }

    public static Nodes.TableWildcardParseNode.Builder convert(TableWildcardParseNode value) {
        if (value == null) return null;

        var builder = Nodes.TableWildcardParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getName();
        if (v1 != null) builder.setName(v1);
        builder.setIsCaseSensitive(value.isCaseSensitive());
        var v2 = value.getTableName();
        if (v2 != null) builder.setTableName(convert(v2));
        builder.setIsRewrite(value.isRewrite());

        return builder;
    }

    public static Nodes.LikeParseNode.Builder convert(LikeParseNode value) {
        if (value == null) return null;

        var builder = Nodes.LikeParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getLikeType();
        if (v1 != null) builder.setLikeType(convert(v1));
        builder.setIsNegate(value.isNegate());

        return builder;
    }

    public static Nodes.AlterIndexStatement.Builder convert(AlterIndexStatement value) {
        if (value == null) return null;

        var builder = Nodes.AlterIndexStatement.newBuilder();

        builder.setBindCount(value.getBindCount());
        var v0 = value.getOperation();
        if (v0 != null) builder.setOperation(convert(v0));
        var v1 = value.getTable();
        if (v1 != null) builder.setTable(convert(v1));
        var v2 = value.getIndexState();
        if (v2 != null) builder.setIndexState(convert(v2));
        var v3 = value.getTableName();
        if (v3 != null) builder.setTableName(v3);
        var v4 = value.getTableType();
        if (v4 != null) builder.setTableType(convert(v4));
        builder.setIfExists(value.ifExists());
        builder.setIsAsync(value.isAsync());

        return builder;
    }

    public static Nodes.RoundParseNode.Builder convert(RoundParseNode value) {
        if (value == null) return null;

        var builder = Nodes.RoundParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getName();
        if (v1 != null) builder.setName(v1);
        builder.setIsAggregate(value.isAggregate());

        return builder;
    }

    public static Nodes.InParseNode.Builder convert(InParseNode value) {
        if (value == null) return null;

        var builder = Nodes.InParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        builder.setIsNegate(value.isNegate());
        builder.setIsSubqueryDistinct(value.isSubqueryDistinct());

        return builder;
    }

    public static Nodes.HintNode.Builder convert(HintNode value) {
        if (value == null) return null;

        var builder = Nodes.HintNode.newBuilder();

        if (value.hasHint(HintNode.Hint.RANGE_SCAN)) {
            builder.addHintsBuilder()
                    .setKey(Nodes.Hint.RANGE_SCAN)
                    .setValue(value.getHint(HintNode.Hint.RANGE_SCAN));
        }

        if (value.hasHint(HintNode.Hint.SKIP_SCAN)) {
            builder.addHintsBuilder()
                    .setKey(Nodes.Hint.SKIP_SCAN)
                    .setValue(value.getHint(HintNode.Hint.SKIP_SCAN));
        }

        if (value.hasHint(HintNode.Hint.NO_CHILD_PARENT_JOIN_OPTIMIZATION)) {
            builder.addHintsBuilder()
                    .setKey(Nodes.Hint.NO_CHILD_PARENT_JOIN_OPTIMIZATION)
                    .setValue(value.getHint(HintNode.Hint.NO_CHILD_PARENT_JOIN_OPTIMIZATION));
        }

        if (value.hasHint(HintNode.Hint.NO_INDEX)) {
            builder.addHintsBuilder()
                    .setKey(Nodes.Hint.NO_INDEX)
                    .setValue(value.getHint(HintNode.Hint.NO_INDEX));
        }

        if (value.hasHint(HintNode.Hint.INDEX)) {
            builder.addHintsBuilder()
                    .setKey(Nodes.Hint.INDEX_1)
                    .setValue(value.getHint(HintNode.Hint.INDEX));
        }

        if (value.hasHint(HintNode.Hint.USE_DATA_OVER_INDEX_TABLE)) {
            builder.addHintsBuilder()
                    .setKey(Nodes.Hint.USE_DATA_OVER_INDEX_TABLE)
                    .setValue(value.getHint(HintNode.Hint.USE_DATA_OVER_INDEX_TABLE));
        }

        if (value.hasHint(HintNode.Hint.USE_INDEX_OVER_DATA_TABLE)) {
            builder.addHintsBuilder()
                    .setKey(Nodes.Hint.USE_INDEX_OVER_DATA_TABLE)
                    .setValue(value.getHint(HintNode.Hint.USE_INDEX_OVER_DATA_TABLE));
        }

        if (value.hasHint(HintNode.Hint.NO_CACHE)) {
            builder.addHintsBuilder()
                    .setKey(Nodes.Hint.NO_CACHE)
                    .setValue(value.getHint(HintNode.Hint.NO_CACHE));
        }

        if (value.hasHint(HintNode.Hint.USE_SORT_MERGE_JOIN)) {
            builder.addHintsBuilder()
                    .setKey(Nodes.Hint.USE_SORT_MERGE_JOIN)
                    .setValue(value.getHint(HintNode.Hint.USE_SORT_MERGE_JOIN));
        }

        if (value.hasHint(HintNode.Hint.NO_STAR_JOIN)) {
            builder.addHintsBuilder()
                    .setKey(Nodes.Hint.NO_STAR_JOIN)
                    .setValue(value.getHint(HintNode.Hint.NO_STAR_JOIN));
        }

        if (value.hasHint(HintNode.Hint.SEEK_TO_COLUMN)) {
            builder.addHintsBuilder()
                    .setKey(Nodes.Hint.SEEK_TO_COLUMN)
                    .setValue(value.getHint(HintNode.Hint.SEEK_TO_COLUMN));
        }

        if (value.hasHint(HintNode.Hint.NO_SEEK_TO_COLUMN)) {
            builder.addHintsBuilder()
                    .setKey(Nodes.Hint.NO_SEEK_TO_COLUMN)
                    .setValue(value.getHint(HintNode.Hint.NO_SEEK_TO_COLUMN));
        }

        if (value.hasHint(HintNode.Hint.SMALL)) {
            builder.addHintsBuilder()
                    .setKey(Nodes.Hint.SMALL)
                    .setValue(value.getHint(HintNode.Hint.SMALL));
        }

        if (value.hasHint(HintNode.Hint.SERIAL)) {
            builder.addHintsBuilder()
                    .setKey(Nodes.Hint.SERIAL)
                    .setValue(value.getHint(HintNode.Hint.SERIAL));
        }

        if (value.hasHint(HintNode.Hint.FORWARD_SCAN)) {
            builder.addHintsBuilder()
                    .setKey(Nodes.Hint.FORWARD_SCAN)
                    .setValue(value.getHint(HintNode.Hint.FORWARD_SCAN));
        }

        return builder;
    }

    public static Nodes.DeclareCursorStatement.Builder convert(DeclareCursorStatement value) {
        if (value == null) return null;

        var builder = Nodes.DeclareCursorStatement.newBuilder();

        builder.setBindCount(value.getBindCount());
        var v0 = value.getOperation();
        if (v0 != null) builder.setOperation(convert(v0));
        var v1 = value.getCursorName();
        if (v1 != null) builder.setCursorName(v1);
        var v2 = value.getQuerySQL();
        if (v2 != null) builder.setQuerySQL(v2);
        var v3 = value.getSelect();
        if (v3 != null) builder.setSelect(convert(v3));
        addAll(value.getSelectOrderBy(), NodeConverter::convert, builder::addSelectOrderBy);

        return builder;
    }

    public static Nodes.P_NamedParseNode.Builder convert(NamedParseNode value) {
        if (value == null) return null;

        var builder = Nodes.P_NamedParseNode.newBuilder();

        if (value instanceof ColumnParseNode) {
            builder.setColumnParseNode(convert((ColumnParseNode) value));
        } else if (value instanceof TableWildcardParseNode) {
            builder.setTableWildcardParseNode(convert((TableWildcardParseNode) value));
        } else if (value instanceof BindParseNode) {
            builder.setBindParseNode(convert((BindParseNode) value));
        } else if (value instanceof FamilyWildcardParseNode) {
            builder.setFamilyWildcardParseNode(convert((FamilyWildcardParseNode) value));
        }

        return builder;
    }

    public static Nodes.EqualParseNode.Builder convert(EqualParseNode value) {
        if (value == null) return null;

        var builder = Nodes.EqualParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getFilterOp();
        if (v1 != null) builder.setFilterOp(convert(v1));
        var v2 = value.getInvertFilterOp();
        if (v2 != null) builder.setInvertFilterOp(convert(v2));

        return builder;
    }

    public static Nodes.LessThanParseNode.Builder convert(LessThanParseNode value) {
        if (value == null) return null;

        var builder = Nodes.LessThanParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getFilterOp();
        if (v1 != null) builder.setFilterOp(convert(v1));
        var v2 = value.getInvertFilterOp();
        if (v2 != null) builder.setInvertFilterOp(convert(v2));

        return builder;
    }

    public static Nodes.GreaterThanParseNode.Builder convert(GreaterThanParseNode value) {
        if (value == null) return null;

        var builder = Nodes.GreaterThanParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getFilterOp();
        if (v1 != null) builder.setFilterOp(convert(v1));
        var v2 = value.getInvertFilterOp();
        if (v2 != null) builder.setInvertFilterOp(convert(v2));

        return builder;
    }

    public static Nodes.NthValueAggregateParseNode.Builder convert(NthValueAggregateParseNode value) {
        if (value == null) return null;

        var builder = Nodes.NthValueAggregateParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getName();
        if (v1 != null) builder.setName(v1);
        builder.setIsAggregate(value.isAggregate());

        return builder;
    }

    public static Nodes.ColumnDef.Builder convert(ColumnDef value) {
        if (value == null) return null;

        var builder = Nodes.ColumnDef.newBuilder();

        var v0 = value.getArraySize();
        if (v0 != null) builder.setArraySize(v0);
        var v1 = value.getColumnDefName();
        if (v1 != null) builder.setColumnDefName(convert(v1));
        var v2 = value.getDataType();
        if (v2 != null) builder.setDataType(convert(v2));
        var v3 = value.getExpression();
        if (v3 != null) builder.setExpression(v3);
        var v4 = value.getMaxLength();
        if (v4 != null) builder.setMaxLength(v4);
        var v5 = value.getScale();
        if (v5 != null) builder.setScale(v5);
        var v6 = value.getSortOrder();
        if (v6 != null) builder.setSortOrder(convert(v6));
        builder.setIsArray(value.isArray());
        builder.setIsNull(value.isNull());
        builder.setIsNullSet(value.isNullSet());
        builder.setIsPK(value.isPK());
        builder.setIsRowTimestamp(value.isRowTimestamp());

        return builder;
    }

    public static Nodes.BindTableNode.Builder convert(BindTableNode value) {
        if (value == null) return null;

        var builder = Nodes.BindTableNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        var v1 = value.getName();
        if (v1 != null) builder.setName(convert(v1));
        var v2 = value.getTableSamplingRate();
        if (v2 != null) builder.setTableSamplingRate(v2);

        return builder;
    }

    public static Nodes.OrParseNode.Builder convert(OrParseNode value) {
        if (value == null) return null;

        var builder = Nodes.OrParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());

        return builder;
    }

    public static Nodes.SortOrder convert(SortOrder value) {
        if (value == SortOrder.ASC) {
            return Nodes.SortOrder.ASC;
        }

        return Nodes.SortOrder.DESC;
    }

    public static Nodes.LikeType convert(LikeParseNode.LikeType value) {
        if (value == LikeParseNode.LikeType.CASE_SENSITIVE) {
            return Nodes.LikeType.CASE_SENSITIVE;
        }

        return Nodes.LikeType.CASE_INSENSITIVE;
    }

    public static Nodes.NotEqualParseNode.Builder convert(NotEqualParseNode value) {
        if (value == null) return null;

        var builder = Nodes.NotEqualParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getFilterOp();
        if (v1 != null) builder.setFilterOp(convert(v1));
        var v2 = value.getInvertFilterOp();
        if (v2 != null) builder.setInvertFilterOp(convert(v2));

        return builder;
    }

    public static Nodes.DeleteStatement.Builder convert(DeleteStatement value) {
        if (value == null) return null;

        var builder = Nodes.DeleteStatement.newBuilder();

        builder.setBindCount(value.getBindCount());
        var v0 = value.getOperation();
        if (v0 != null) builder.setOperation(convert(v0));
        var v1 = value.getTable();
        if (v1 != null) builder.setTable(convert(v1));
        var v2 = value.getHint();
        if (v2 != null && !v2.isEmpty()) builder.setHint(convert(v2));
        var v3 = value.getLimit();
        if (v3 != null) builder.setLimit(convert(v3));
        var v4 = value.getOffset();
        if (v4 != null) builder.setOffset(convert(v4));
        addAll(value.getOrderBy(), NodeConverter::convert, builder::addOrderBy);
        var v5 = value.getWhere();
        if (v5 != null) builder.setWhere(convert(v5));
        builder.setIsAggregate(value.isAggregate());
        builder.setIsDistinct(value.isDistinct());

        return builder;
    }

    public static Nodes.ArrayModifierParseNode.Builder convert(ArrayModifierParseNode value) {
        if (value == null) return null;

        var builder = Nodes.ArrayModifierParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getName();
        if (v1 != null) builder.setName(v1);
        builder.setIsAggregate(value.isAggregate());

        return builder;
    }

    public static Nodes.InListParseNode.Builder convert(InListParseNode value) {
        if (value == null) return null;

        var builder = Nodes.InListParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        builder.setIsNegate(value.isNegate());

        return builder;
    }

    public static Nodes.ChangePermsStatement.Builder convert(ChangePermsStatement value) {
        if (value == null) return null;

        var builder = Nodes.ChangePermsStatement.newBuilder();

        builder.setBindCount(value.getBindCount());
        var v0 = value.getOperation();
        if (v0 != null) builder.setOperation(convert(v0));
        var v1 = value.getName();
        if (v1 != null) builder.setName(v1);
        addAll(value.getPermsList(), NodeConverter::convert, builder::addPermsList);
        var v2 = value.getSchemaName();
        if (v2 != null) builder.setSchemaName(v2);
        var v3 = value.getTableName();
        if (v3 != null) builder.setTableName(convert(v3));
        builder.setIsGrantStatement(value.isGrantStatement());

        return builder;
    }

    public static Nodes.CreateTableStatement.Builder convert(CreateTableStatement value) {
        if (value == null) return null;

        var builder = Nodes.CreateTableStatement.newBuilder();

        builder.setBindCount(value.getBindCount());
        var v0 = value.getOperation();
        if (v0 != null) builder.setOperation(convert(v0));
        var v1 = value.getBaseTableName();
        if (v1 != null) builder.setBaseTableName(convert(v1));
        addAll(value.getColumnDefs(), NodeConverter::convert, builder::addColumnDefs);
        var v2 = value.getPrimaryKeyConstraint();
        if (v2 != null) builder.setPrimaryKeyConstraint(convert(v2));
        addAll(value.getSplitNodes(), NodeConverter::convert, builder::addSplitNodes);
        var v3 = value.getTableName();
        if (v3 != null) builder.setTableName(convert(v3));
        var v4 = value.getTableType();
        if (v4 != null) builder.setTableType(convert(v4));
        var v5 = value.getWhereClause();
        if (v5 != null) builder.setWhereClause(convert(v5));
        builder.setIfNotExists(value.ifNotExists());
        var v6 = value.immutableRows();
        if (v6 != null) builder.setImmutableRows(v6);

        return builder;
    }

    public static Nodes.DropSequenceStatement.Builder convert(DropSequenceStatement value) {
        if (value == null) return null;

        var builder = Nodes.DropSequenceStatement.newBuilder();

        builder.setBindCount(value.getBindCount());
        var v0 = value.getOperation();
        if (v0 != null) builder.setOperation(convert(v0));
        var v1 = value.getSequenceName();
        if (v1 != null) builder.setSequenceName(convert(v1));
        builder.setIfExists(value.ifExists());

        return builder;
    }

    public static Nodes.PTableType convert(PTableType value) {
        if (value == PTableType.SYSTEM) {
            return Nodes.PTableType.SYSTEM;
        } else if (value == PTableType.TABLE) {
            return Nodes.PTableType.TABLE;
        } else if (value == PTableType.VIEW) {
            return Nodes.PTableType.VIEW;
        } else if (value == PTableType.INDEX) {
            return Nodes.PTableType.INDEX;
        } else if (value == PTableType.PROJECTED) {
            return Nodes.PTableType.PROJECTED;
        }

        return Nodes.PTableType.SUBQUERY;
    }

    public static Nodes.AliasedNode.Builder convert(AliasedNode value) {
        if (value == null) return null;

        var builder = Nodes.AliasedNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        var v1 = value.getNode();
        if (v1 != null) builder.setNode(convert(v1));
        builder.setIsCaseSensitve(value.isCaseSensitve());

        return builder;
    }

    public static Nodes.RegexpSubstrParseNode.Builder convert(RegexpSubstrParseNode value) {
        if (value == null) return null;

        var builder = Nodes.RegexpSubstrParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getName();
        if (v1 != null) builder.setName(v1);
        builder.setIsAggregate(value.isAggregate());

        return builder;
    }

    public static Nodes.I_BindableStatement.Builder convert(BindableStatement value) {
        if (value == null) return null;

        var builder = Nodes.I_BindableStatement.newBuilder();

        if (value instanceof AlterSessionStatement) {
            builder.setAlterSessionStatement(convert((AlterSessionStatement) value));
        } else if (value instanceof DeleteStatement) {
            builder.setDeleteStatement(convert((DeleteStatement) value));
        } else if (value instanceof UpsertStatement) {
            builder.setUpsertStatement(convert((UpsertStatement) value));
        } else if (value instanceof UpdateStatisticsStatement) {
            builder.setUpdateStatisticsStatement(convert((UpdateStatisticsStatement) value));
        } else if (value instanceof AlterIndexStatement) {
            builder.setAlterIndexStatement(convert((AlterIndexStatement) value));
        } else if (value instanceof DropColumnStatement) {
            builder.setDropColumnStatement(convert((DropColumnStatement) value));
        } else if (value instanceof AddColumnStatement) {
            builder.setAddColumnStatement(convert((AddColumnStatement) value));
        } else if (value instanceof CreateIndexStatement) {
            builder.setCreateIndexStatement(convert((CreateIndexStatement) value));
        } else if (value instanceof DropTableStatement) {
            builder.setDropTableStatement(convert((DropTableStatement) value));
        } else if (value instanceof CreateSequenceStatement) {
            builder.setCreateSequenceStatement(convert((CreateSequenceStatement) value));
        } else if (value instanceof CreateTableStatement) {
            builder.setCreateTableStatement(convert((CreateTableStatement) value));
        } else if (value instanceof DropSequenceStatement) {
            builder.setDropSequenceStatement(convert((DropSequenceStatement) value));
        } else if (value instanceof DropIndexStatement) {
            builder.setDropIndexStatement(convert((DropIndexStatement) value));
        } else if (value instanceof DeleteJarStatement) {
            builder.setDeleteJarStatement(convert((DeleteJarStatement) value));
        } else if (value instanceof UseSchemaStatement) {
            builder.setUseSchemaStatement(convert((UseSchemaStatement) value));
        } else if (value instanceof DropFunctionStatement) {
            builder.setDropFunctionStatement(convert((DropFunctionStatement) value));
        } else if (value instanceof CreateFunctionStatement) {
            builder.setCreateFunctionStatement(convert((CreateFunctionStatement) value));
        } else if (value instanceof DropSchemaStatement) {
            builder.setDropSchemaStatement(convert((DropSchemaStatement) value));
        } else if (value instanceof AddJarsStatement) {
            builder.setAddJarsStatement(convert((AddJarsStatement) value));
        } else if (value instanceof CreateSchemaStatement) {
            builder.setCreateSchemaStatement(convert((CreateSchemaStatement) value));
        } else if (value instanceof FetchStatement) {
            builder.setFetchStatement(convert((FetchStatement) value));
        } else if (value instanceof ListJarsStatement) {
            builder.setListJarsStatement(convert((ListJarsStatement) value));
        } else if (value instanceof SelectStatement) {
            builder.setSelectStatement(convert((SelectStatement) value));
        } else if (value instanceof DeclareCursorStatement) {
            builder.setDeclareCursorStatement(convert((DeclareCursorStatement) value));
        } else if (value instanceof ChangePermsStatement) {
            builder.setChangePermsStatement(convert((ChangePermsStatement) value));
        } else if (value instanceof TraceStatement) {
            builder.setTraceStatement(convert((TraceStatement) value));
        } else if (value instanceof OpenStatement) {
            builder.setOpenStatement(convert((OpenStatement) value));
        } else if (value instanceof ExplainStatement) {
            builder.setExplainStatement(convert((ExplainStatement) value));
        } else if (value instanceof ExecuteUpgradeStatement) {
            builder.setExecuteUpgradeStatement(convert((ExecuteUpgradeStatement) value));
        } else if (value instanceof CloseStatement) {
            builder.setCloseStatement(convert((CloseStatement) value));
        } else if (value instanceof DMLStatement) {
            builder.setDMLStatement(convertDefault((DMLStatement) value));
        }

        return builder;
    }

    public static Nodes.SelectStatement.Builder convert(SelectStatement value) {
        if (value == null) return null;

        var builder = Nodes.SelectStatement.newBuilder();

        builder.setBindCount(value.getBindCount());
        var v0 = value.getOperation();
        if (v0 != null) builder.setOperation(convert(v0));
        var v1 = value.getHint();
        if (v1 != null && !v1.isEmpty()) builder.setHint(convert(v1));
        var v2 = value.getLimit();
        if (v2 != null) builder.setLimit(convert(v2));
        var v3 = value.getOffset();
        if (v3 != null) builder.setOffset(convert(v3));
        addAll(value.getOrderBy(), NodeConverter::convert, builder::addOrderBy);
        var v4 = value.getTableSamplingRate();
        if (v4 != null) builder.setTableSamplingRate(v4);
        var v5 = value.getWhere();
        if (v5 != null) builder.setWhere(convert(v5));
        builder.setIsAggregate(value.isAggregate());
        builder.setIsDistinct(value.isDistinct());
        var v6 = value.getFrom();
        if (v6 != null) builder.setFrom(convert(v6));
        addAll(value.getGroupBy(), NodeConverter::convert, builder::addGroupBy);
        var v7 = value.getHaving();
        if (v7 != null) builder.setHaving(convert(v7));
        addAll(value.getSelect(), NodeConverter::convert, builder::addSelect);
        addAll(value.getSelects(), NodeConverter::convert, builder::addSelects);
        builder.setIsJoin(value.isJoin());
        builder.setIsUnion(value.isUnion());

        return builder;
    }

    public static Nodes.Operation convert(PhoenixStatement.Operation value) {
        if (value == PhoenixStatement.Operation.QUERY) {
            return Nodes.Operation.QUERY;
        } else if (value == PhoenixStatement.Operation.DELETE) {
            return Nodes.Operation.DELETE;
        } else if (value == PhoenixStatement.Operation.UPSERT) {
            return Nodes.Operation.UPSERT;
        } else if (value == PhoenixStatement.Operation.UPGRADE) {
            return Nodes.Operation.UPGRADE;
        }

        return Nodes.Operation.ADMIN_1;
    }

    public static Nodes.P_AlterTableStatement.Builder convert(AlterTableStatement value) {
        if (value == null) return null;

        var builder = Nodes.P_AlterTableStatement.newBuilder();

        if (value instanceof DropColumnStatement) {
            builder.setDropColumnStatement(convert((DropColumnStatement) value));
        } else if (value instanceof AddColumnStatement) {
            builder.setAddColumnStatement(convert((AddColumnStatement) value));
        }

        return builder;
    }

    public static Nodes.TraceStatement.Builder convert(TraceStatement value) {
        if (value == null) return null;

        var builder = Nodes.TraceStatement.newBuilder();

        builder.setBindCount(value.getBindCount());
        var v0 = value.getOperation();
        if (v0 != null) builder.setOperation(convert(v0));
        builder.setSamplingRate(value.getSamplingRate());
        builder.setIsTraceOn(value.isTraceOn());

        return builder;
    }

    public static Nodes.P_UnaryParseNode.Builder convert(UnaryParseNode value) {
        if (value == null) return null;

        var builder = Nodes.P_UnaryParseNode.newBuilder();

        if (value instanceof NotParseNode) {
            builder.setNotParseNode(convert((NotParseNode) value));
        } else if (value instanceof IsNullParseNode) {
            builder.setIsNullParseNode(convert((IsNullParseNode) value));
        } else if (value instanceof CastParseNode) {
            builder.setCastParseNode(convert((CastParseNode) value));
        } else if (value instanceof ExistsParseNode) {
            builder.setExistsParseNode(convert((ExistsParseNode) value));
        }

        return builder;
    }

    public static Nodes.MinAggregateParseNode.Builder convert(MinAggregateParseNode value) {
        if (value == null) return null;

        var builder = Nodes.MinAggregateParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getName();
        if (v1 != null) builder.setName(v1);
        builder.setIsAggregate(value.isAggregate());

        return builder;
    }

    public static Nodes.FloorParseNode.Builder convert(FloorParseNode value) {
        if (value == null) return null;

        var builder = Nodes.FloorParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getName();
        if (v1 != null) builder.setName(v1);
        builder.setIsAggregate(value.isAggregate());

        return builder;
    }

    public static Nodes.ToDateParseNode.Builder convert(ToDateParseNode value) {
        if (value == null) return null;

        var builder = Nodes.ToDateParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getName();
        if (v1 != null) builder.setName(v1);
        builder.setIsAggregate(value.isAggregate());

        return builder;
    }

    public static Nodes.DropColumnStatement.Builder convert(DropColumnStatement value) {
        if (value == null) return null;

        var builder = Nodes.DropColumnStatement.newBuilder();

        builder.setBindCount(value.getBindCount());
        var v0 = value.getOperation();
        if (v0 != null) builder.setOperation(convert(v0));
        var v1 = value.getTable();
        if (v1 != null) builder.setTable(convert(v1));
        var v2 = value.getTableType();
        if (v2 != null) builder.setTableType(convert(v2));
        addAll(value.getColumnRefs(), NodeConverter::convert, builder::addColumnRefs);
        builder.setIfExists(value.ifExists());

        return builder;
    }

    public static Nodes.OpenStatement.Builder convert(OpenStatement value) {
        if (value == null) return null;

        var builder = Nodes.OpenStatement.newBuilder();

        builder.setBindCount(value.getBindCount());
        var v0 = value.getOperation();
        if (v0 != null) builder.setOperation(convert(v0));
        var v1 = value.getCursorName();
        if (v1 != null) builder.setCursorName(v1);

        return builder;
    }

    public static Nodes.PFunction.Builder convert(PFunction value) {
        if (value == null) return null;

        var builder = Nodes.PFunction.newBuilder();

        var v0 = value.getClassName();
        if (v0 != null) builder.setClassName(v0);
        addAll(value.getFunctionArguments(), NodeConverter::convert, builder::addFunctionArguments);
        var v1 = value.getFunctionName();
        if (v1 != null) builder.setFunctionName(v1);
        var v2 = value.getJarPath();
        if (v2 != null) builder.setJarPath(v2);
        var v3 = value.getKey();
        if (v3 != null) builder.setKey(convert(v3));
        var v4 = value.getReturnType();
        if (v4 != null) builder.setReturnType(v4);
        var v5 = value.getTenantId();
        if (v5 != null) builder.setTenantId(convert(v5));
        builder.setTimeStamp(value.getTimeStamp());
        builder.setIsReplace(value.isReplace());
        builder.setIsTemporaryFunction(value.isTemporaryFunction());

        return builder;
    }

    public static Nodes.BindParseNode.Builder convert(BindParseNode value) {
        if (value == null) return null;

        var builder = Nodes.BindParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getName();
        if (v1 != null) builder.setName(v1);
        builder.setIsCaseSensitive(value.isCaseSensitive());
        builder.setIndex(value.getIndex());

        return builder;
    }

    public static Nodes.LessThanOrEqualParseNode.Builder convert(LessThanOrEqualParseNode value) {
        if (value == null) return null;

        var builder = Nodes.LessThanOrEqualParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getFilterOp();
        if (v1 != null) builder.setFilterOp(convert(v1));
        var v2 = value.getInvertFilterOp();
        if (v2 != null) builder.setInvertFilterOp(convert(v2));

        return builder;
    }

    public static Nodes.PrimaryKeyConstraint.Pair_ColumnName_SortOrder.Builder convertPairColumnNameSortOrder(Pair<ColumnName, SortOrder> value) {
        if (value == null) return null;

        var builder = Nodes.PrimaryKeyConstraint.Pair_ColumnName_SortOrder.newBuilder();

        builder.setFirst(convert(value.getFirst()));
        builder.setSecond(convert(value.getSecond()));


        return builder;
    }

    public static Nodes.PrimaryKeyConstraint.Builder convert(PrimaryKeyConstraint value) {
        if (value == null) return null;

        var builder = Nodes.PrimaryKeyConstraint.newBuilder();

        var v0 = value.getName();
        if (v0 != null) builder.setName(v0);
        builder.setIsCaseSensitive(value.isCaseSensitive());
        addAll(value.getColumnNames(), NodeConverter::convertPairColumnNameSortOrder, builder::addColumnNames);
        builder.setNumColumnsWithRowTimestamp(value.getNumColumnsWithRowTimestamp());

        return builder;
    }

    public static Nodes.DropIndexStatement.Builder convert(DropIndexStatement value) {
        if (value == null) return null;

        var builder = Nodes.DropIndexStatement.newBuilder();

        builder.setBindCount(value.getBindCount());
        var v0 = value.getOperation();
        if (v0 != null) builder.setOperation(convert(v0));
        var v1 = value.getIndexName();
        if (v1 != null) builder.setIndexName(convert(v1));
        var v2 = value.getTableName();
        if (v2 != null) builder.setTableName(convert(v2));
        builder.setIfExists(value.ifExists());

        return builder;
    }

    public static Nodes.DeleteJarStatement.Builder convert(DeleteJarStatement value) {
        if (value == null) return null;

        var builder = Nodes.DeleteJarStatement.newBuilder();

        builder.setBindCount(value.getBindCount());
        var v0 = value.getOperation();
        if (v0 != null) builder.setOperation(convert(v0));
        var v1 = value.getJarPath();
        if (v1 != null) builder.setJarPath(convert(v1));

        return builder;
    }

    public static Nodes.GreaterThanOrEqualParseNode.Builder convert(GreaterThanOrEqualParseNode value) {
        if (value == null) return null;

        var builder = Nodes.GreaterThanOrEqualParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getFilterOp();
        if (v1 != null) builder.setFilterOp(convert(v1));
        var v2 = value.getInvertFilterOp();
        if (v2 != null) builder.setInvertFilterOp(convert(v2));

        return builder;
    }

    public static Nodes.RegexpSplitParseNode.Builder convert(RegexpSplitParseNode value) {
        if (value == null) return null;

        var builder = Nodes.RegexpSplitParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getName();
        if (v1 != null) builder.setName(v1);
        builder.setIsAggregate(value.isAggregate());

        return builder;
    }

    public static Nodes.P_MutableStatement.Builder convert(MutableStatement value) {
        if (value == null) return null;

        var builder = Nodes.P_MutableStatement.newBuilder();

        if (value instanceof AlterSessionStatement) {
            builder.setAlterSessionStatement(convert((AlterSessionStatement) value));
        } else if (value instanceof DeleteStatement) {
            builder.setDeleteStatement(convert((DeleteStatement) value));
        } else if (value instanceof UpsertStatement) {
            builder.setUpsertStatement(convert((UpsertStatement) value));
        } else if (value instanceof UpdateStatisticsStatement) {
            builder.setUpdateStatisticsStatement(convert((UpdateStatisticsStatement) value));
        } else if (value instanceof AlterIndexStatement) {
            builder.setAlterIndexStatement(convert((AlterIndexStatement) value));
        } else if (value instanceof DropColumnStatement) {
            builder.setDropColumnStatement(convert((DropColumnStatement) value));
        } else if (value instanceof AddColumnStatement) {
            builder.setAddColumnStatement(convert((AddColumnStatement) value));
        } else if (value instanceof CreateIndexStatement) {
            builder.setCreateIndexStatement(convert((CreateIndexStatement) value));
        } else if (value instanceof DropTableStatement) {
            builder.setDropTableStatement(convert((DropTableStatement) value));
        } else if (value instanceof CreateSequenceStatement) {
            builder.setCreateSequenceStatement(convert((CreateSequenceStatement) value));
        } else if (value instanceof CreateTableStatement) {
            builder.setCreateTableStatement(convert((CreateTableStatement) value));
        } else if (value instanceof DropSequenceStatement) {
            builder.setDropSequenceStatement(convert((DropSequenceStatement) value));
        } else if (value instanceof DropIndexStatement) {
            builder.setDropIndexStatement(convert((DropIndexStatement) value));
        } else if (value instanceof DeleteJarStatement) {
            builder.setDeleteJarStatement(convert((DeleteJarStatement) value));
        } else if (value instanceof UseSchemaStatement) {
            builder.setUseSchemaStatement(convert((UseSchemaStatement) value));
        } else if (value instanceof DropFunctionStatement) {
            builder.setDropFunctionStatement(convert((DropFunctionStatement) value));
        } else if (value instanceof CreateFunctionStatement) {
            builder.setCreateFunctionStatement(convert((CreateFunctionStatement) value));
        } else if (value instanceof DropSchemaStatement) {
            builder.setDropSchemaStatement(convert((DropSchemaStatement) value));
        } else if (value instanceof AddJarsStatement) {
            builder.setAddJarsStatement(convert((AddJarsStatement) value));
        } else if (value instanceof CreateSchemaStatement) {
            builder.setCreateSchemaStatement(convert((CreateSchemaStatement) value));
        } else if (value instanceof DMLStatement) {
            builder.setDMLStatement(convertDefault((DMLStatement) value));
        }

        return builder;
    }

    public static Nodes.AggregateFunctionWithinGroupParseNode.Builder convert(AggregateFunctionWithinGroupParseNode value) {
        if (value == null) return null;

        var builder = Nodes.AggregateFunctionWithinGroupParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getName();
        if (v1 != null) builder.setName(v1);
        builder.setIsAggregate(value.isAggregate());

        return builder;
    }

    public static Nodes.P_ParseNode.Builder convert(ParseNode value) {
        if (value == null) return null;

        var builder = Nodes.P_ParseNode.newBuilder();

        if (value instanceof RowValueConstructorParseNode) {
            builder.setRowValueConstructorParseNode(convert((RowValueConstructorParseNode) value));
        } else if (value instanceof UDFParseNode) {
            builder.setUDFParseNode(convert((UDFParseNode) value));
        } else if (value instanceof FirstValuesAggregateParseNode) {
            builder.setFirstValuesAggregateParseNode(convert((FirstValuesAggregateParseNode) value));
        } else if (value instanceof NthValueAggregateParseNode) {
            builder.setNthValueAggregateParseNode(convert((NthValueAggregateParseNode) value));
        } else if (value instanceof MinAggregateParseNode) {
            builder.setMinAggregateParseNode(convert((MinAggregateParseNode) value));
        } else if (value instanceof DistinctCountParseNode) {
            builder.setDistinctCountParseNode(convert((DistinctCountParseNode) value));
        } else if (value instanceof LastValuesAggregateParseNode) {
            builder.setLastValuesAggregateParseNode(convert((LastValuesAggregateParseNode) value));
        } else if (value instanceof SumAggregateParseNode) {
            builder.setSumAggregateParseNode(convert((SumAggregateParseNode) value));
        } else if (value instanceof FirstValueAggregateParseNode) {
            builder.setFirstValueAggregateParseNode(convert((FirstValueAggregateParseNode) value));
        } else if (value instanceof LastValueAggregateParseNode) {
            builder.setLastValueAggregateParseNode(convert((LastValueAggregateParseNode) value));
        } else if (value instanceof DistinctCountHyperLogLogAggregateParseNode) {
            builder.setDistinctCountHyperLogLogAggregateParseNode(convert((DistinctCountHyperLogLogAggregateParseNode) value));
        } else if (value instanceof MaxAggregateParseNode) {
            builder.setMaxAggregateParseNode(convert((MaxAggregateParseNode) value));
        } else if (value instanceof AggregateFunctionWithinGroupParseNode) {
            builder.setAggregateFunctionWithinGroupParseNode(convert((AggregateFunctionWithinGroupParseNode) value));
        } else if (value instanceof AvgAggregateParseNode) {
            builder.setAvgAggregateParseNode(convert((AvgAggregateParseNode) value));
        } else if (value instanceof RegexpReplaceParseNode) {
            builder.setRegexpReplaceParseNode(convert((RegexpReplaceParseNode) value));
        } else if (value instanceof ToTimeParseNode) {
            builder.setToTimeParseNode(convert((ToTimeParseNode) value));
        } else if (value instanceof CurrentTimeParseNode) {
            builder.setCurrentTimeParseNode(convert((CurrentTimeParseNode) value));
        } else if (value instanceof ToNumberParseNode) {
            builder.setToNumberParseNode(convert((ToNumberParseNode) value));
        } else if (value instanceof RoundParseNode) {
            builder.setRoundParseNode(convert((RoundParseNode) value));
        } else if (value instanceof ArrayModifierParseNode) {
            builder.setArrayModifierParseNode(convert((ArrayModifierParseNode) value));
        } else if (value instanceof RegexpSubstrParseNode) {
            builder.setRegexpSubstrParseNode(convert((RegexpSubstrParseNode) value));
        } else if (value instanceof FloorParseNode) {
            builder.setFloorParseNode(convert((FloorParseNode) value));
        } else if (value instanceof ToDateParseNode) {
            builder.setToDateParseNode(convert((ToDateParseNode) value));
        } else if (value instanceof RegexpSplitParseNode) {
            builder.setRegexpSplitParseNode(convert((RegexpSplitParseNode) value));
        } else if (value instanceof CurrentDateParseNode) {
            builder.setCurrentDateParseNode(convert((CurrentDateParseNode) value));
        } else if (value instanceof CeilParseNode) {
            builder.setCeilParseNode(convert((CeilParseNode) value));
        } else if (value instanceof ToTimestampParseNode) {
            builder.setToTimestampParseNode(convert((ToTimestampParseNode) value));
        } else if (value instanceof ToCharParseNode) {
            builder.setToCharParseNode(convert((ToCharParseNode) value));
        } else if (value instanceof BetweenParseNode) {
            builder.setBetweenParseNode(convert((BetweenParseNode) value));
        } else if (value instanceof ArrayAnyComparisonNode) {
            builder.setArrayAnyComparisonNode(convert((ArrayAnyComparisonNode) value));
        } else if (value instanceof ArrayAllComparisonNode) {
            builder.setArrayAllComparisonNode(convert((ArrayAllComparisonNode) value));
        } else if (value instanceof EqualParseNode) {
            builder.setEqualParseNode(convert((EqualParseNode) value));
        } else if (value instanceof LessThanParseNode) {
            builder.setLessThanParseNode(convert((LessThanParseNode) value));
        } else if (value instanceof GreaterThanParseNode) {
            builder.setGreaterThanParseNode(convert((GreaterThanParseNode) value));
        } else if (value instanceof NotEqualParseNode) {
            builder.setNotEqualParseNode(convert((NotEqualParseNode) value));
        } else if (value instanceof LessThanOrEqualParseNode) {
            builder.setLessThanOrEqualParseNode(convert((LessThanOrEqualParseNode) value));
        } else if (value instanceof GreaterThanOrEqualParseNode) {
            builder.setGreaterThanOrEqualParseNode(convert((GreaterThanOrEqualParseNode) value));
        } else if (value instanceof LikeParseNode) {
            builder.setLikeParseNode(convert((LikeParseNode) value));
        } else if (value instanceof InParseNode) {
            builder.setInParseNode(convert((InParseNode) value));
        } else if (value instanceof ArrayElemRefNode) {
            builder.setArrayElemRefNode(convert((ArrayElemRefNode) value));
        } else if (value instanceof NotParseNode) {
            builder.setNotParseNode(convert((NotParseNode) value));
        } else if (value instanceof IsNullParseNode) {
            builder.setIsNullParseNode(convert((IsNullParseNode) value));
        } else if (value instanceof CastParseNode) {
            builder.setCastParseNode(convert((CastParseNode) value));
        } else if (value instanceof ExistsParseNode) {
            builder.setExistsParseNode(convert((ExistsParseNode) value));
        } else if (value instanceof OrParseNode) {
            builder.setOrParseNode(convert((OrParseNode) value));
        } else if (value instanceof InListParseNode) {
            builder.setInListParseNode(convert((InListParseNode) value));
        } else if (value instanceof AndParseNode) {
            builder.setAndParseNode(convert((AndParseNode) value));
        } else if (value instanceof SubtractParseNode) {
            builder.setSubtractParseNode(convert((SubtractParseNode) value));
        } else if (value instanceof ModulusParseNode) {
            builder.setModulusParseNode(convert((ModulusParseNode) value));
        } else if (value instanceof MultiplyParseNode) {
            builder.setMultiplyParseNode(convert((MultiplyParseNode) value));
        } else if (value instanceof AddParseNode) {
            builder.setAddParseNode(convert((AddParseNode) value));
        } else if (value instanceof DivideParseNode) {
            builder.setDivideParseNode(convert((DivideParseNode) value));
        } else if (value instanceof ArrayConstructorNode) {
            builder.setArrayConstructorNode(convert((ArrayConstructorNode) value));
        } else if (value instanceof StringConcatParseNode) {
            builder.setStringConcatParseNode(convert((StringConcatParseNode) value));
        } else if (value instanceof CaseParseNode) {
            builder.setCaseParseNode(convert((CaseParseNode) value));
        } else if (value instanceof ColumnParseNode) {
            builder.setColumnParseNode(convert((ColumnParseNode) value));
        } else if (value instanceof TableWildcardParseNode) {
            builder.setTableWildcardParseNode(convert((TableWildcardParseNode) value));
        } else if (value instanceof BindParseNode) {
            builder.setBindParseNode(convert((BindParseNode) value));
        } else if (value instanceof FamilyWildcardParseNode) {
            builder.setFamilyWildcardParseNode(convert((FamilyWildcardParseNode) value));
        } else if (value instanceof SequenceValueParseNode) {
            builder.setSequenceValueParseNode(convert((SequenceValueParseNode) value));
        } else if (value instanceof SubqueryParseNode) {
            builder.setSubqueryParseNode(convert((SubqueryParseNode) value));
        } else if (value instanceof LiteralParseNode) {
            builder.setLiteralParseNode(convert((LiteralParseNode) value));
        } else if (value instanceof WildcardParseNode) {
            builder.setWildcardParseNode(convert((WildcardParseNode) value));
        } else if (value instanceof AggregateFunctionParseNode) {
            builder.setAggregateFunctionParseNode(convertDefault((AggregateFunctionParseNode) value));
        } else if (value instanceof FunctionParseNode) {
            builder.setFunctionParseNode(convertDefault((FunctionParseNode) value));
        }

        return builder;
    }

    public static Nodes.ExplainStatement.Builder convert(ExplainStatement value) {
        if (value == null) return null;

        var builder = Nodes.ExplainStatement.newBuilder();

        builder.setBindCount(value.getBindCount());
        var v0 = value.getOperation();
        if (v0 != null) builder.setOperation(convert(v0));
        var v1 = value.getStatement();
        if (v1 != null) builder.setStatement(convert(v1));

        return builder;
    }

    public static Nodes.UseSchemaStatement.Builder convert(UseSchemaStatement value) {
        if (value == null) return null;

        var builder = Nodes.UseSchemaStatement.newBuilder();

        builder.setBindCount(value.getBindCount());
        var v0 = value.getOperation();
        if (v0 != null) builder.setOperation(convert(v0));
        var v1 = value.getSchemaName();
        if (v1 != null) builder.setSchemaName(v1);

        return builder;
    }

    public static Nodes.AndParseNode.Builder convert(AndParseNode value) {
        if (value == null) return null;

        var builder = Nodes.AndParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());

        return builder;
    }

    public static Nodes.DistinctCountParseNode.Builder convert(DistinctCountParseNode value) {
        if (value == null) return null;

        var builder = Nodes.DistinctCountParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getName();
        if (v1 != null) builder.setName(v1);
        builder.setIsAggregate(value.isAggregate());

        return builder;
    }

    public static Nodes.JoinType convert(JoinTableNode.JoinType value) {
        if (value == JoinTableNode.JoinType.Inner) {
            return Nodes.JoinType.Inner;
        } else if (value == JoinTableNode.JoinType.Left) {
            return Nodes.JoinType.Left;
        } else if (value == JoinTableNode.JoinType.Right) {
            return Nodes.JoinType.Right;
        } else if (value == JoinTableNode.JoinType.Full) {
            return Nodes.JoinType.Full;
        } else if (value == JoinTableNode.JoinType.Semi) {
            return Nodes.JoinType.Semi;
        }

        return Nodes.JoinType.Anti;
    }

    public static Nodes.CurrentDateParseNode.Builder convert(CurrentDateParseNode value) {
        if (value == null) return null;

        var builder = Nodes.CurrentDateParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getName();
        if (v1 != null) builder.setName(v1);
        builder.setIsAggregate(value.isAggregate());

        return builder;
    }

    public static Nodes.NamedNode.Builder convertDefault(NamedNode value) {
        if (value == null) return null;

        var builder = Nodes.NamedNode.newBuilder();

        var v0 = value.getName();
        if (v0 != null) builder.setName(v0);
        builder.setIsCaseSensitive(value.isCaseSensitive());

        return builder;
    }

    public static Nodes.P_NamedNode.Builder convert(NamedNode value) {
        if (value == null) return null;

        var builder = Nodes.P_NamedNode.newBuilder();

        if (value instanceof PrimaryKeyConstraint) {
            builder.setPrimaryKeyConstraint(convert((PrimaryKeyConstraint) value));
        } else if (value instanceof NamedNode) {
            builder.setNamedNode(convertDefault((NamedNode) value));
        }

        return builder;
    }

    public static Nodes.CeilParseNode.Builder convert(CeilParseNode value) {
        if (value == null) return null;

        var builder = Nodes.CeilParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getName();
        if (v1 != null) builder.setName(v1);
        builder.setIsAggregate(value.isAggregate());

        return builder;
    }

    public static Nodes.IsNullParseNode.Builder convert(IsNullParseNode value) {
        if (value == null) return null;

        var builder = Nodes.IsNullParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        builder.setIsNegate(value.isNegate());

        return builder;
    }

    public static Nodes.DropFunctionStatement.Builder convert(DropFunctionStatement value) {
        if (value == null) return null;

        var builder = Nodes.DropFunctionStatement.newBuilder();

        builder.setBindCount(value.getBindCount());
        var v0 = value.getOperation();
        if (v0 != null) builder.setOperation(convert(v0));
        var v1 = value.getFunctionName();
        if (v1 != null) builder.setFunctionName(v1);
        builder.setIfExists(value.ifExists());

        return builder;
    }

    public static Nodes.IndexKeyConstraint.Pair_ParseNode_SortOrder.Builder convertPairParseNodeSortOrder(Pair<ParseNode, SortOrder> value) {
        if (value == null) return null;

        var builder = Nodes.IndexKeyConstraint.Pair_ParseNode_SortOrder.newBuilder();

        builder.setFirst(convert(value.getFirst()));
        builder.setSecond(convert(value.getSecond()));


        return builder;
    }

    public static Nodes.IndexKeyConstraint.Builder convert(IndexKeyConstraint value) {
        if (value == null) return null;

        var builder = Nodes.IndexKeyConstraint.newBuilder();

        addAll(value.getParseNodeAndSortOrderList(), NodeConverter::convertPairParseNodeSortOrder, builder::addParseNodeAndSortOrderList);

        return builder;
    }

    public static Nodes.CreateFunctionStatement.Builder convert(CreateFunctionStatement value) {
        if (value == null) return null;

        var builder = Nodes.CreateFunctionStatement.newBuilder();

        builder.setBindCount(value.getBindCount());
        var v0 = value.getOperation();
        if (v0 != null) builder.setOperation(convert(v0));
        var v1 = value.getFunctionInfo();
        if (v1 != null) builder.setFunctionInfo(convert(v1));
        builder.setIsReplace(value.isReplace());
        builder.setIsTemporary(value.isTemporary());

        return builder;
    }

    public static Nodes.LastValuesAggregateParseNode.Builder convert(LastValuesAggregateParseNode value) {
        if (value == null) return null;

        var builder = Nodes.LastValuesAggregateParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getName();
        if (v1 != null) builder.setName(v1);
        builder.setIsAggregate(value.isAggregate());

        return builder;
    }

    public static Nodes.SumAggregateParseNode.Builder convert(SumAggregateParseNode value) {
        if (value == null) return null;

        var builder = Nodes.SumAggregateParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getName();
        if (v1 != null) builder.setName(v1);
        builder.setIsAggregate(value.isAggregate());

        return builder;
    }

    public static Nodes.SubtractParseNode.Builder convert(SubtractParseNode value) {
        if (value == null) return null;

        var builder = Nodes.SubtractParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getOperator();
        if (v1 != null) builder.setOperator(v1);

        return builder;
    }

    public static Nodes.FirstValueAggregateParseNode.Builder convert(FirstValueAggregateParseNode value) {
        if (value == null) return null;

        var builder = Nodes.FirstValueAggregateParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getName();
        if (v1 != null) builder.setName(v1);
        builder.setIsAggregate(value.isAggregate());

        return builder;
    }

    public static Nodes.ModulusParseNode.Builder convert(ModulusParseNode value) {
        if (value == null) return null;

        var builder = Nodes.ModulusParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getOperator();
        if (v1 != null) builder.setOperator(v1);

        return builder;
    }

    public static Nodes.CastParseNode.Builder convert(CastParseNode value) {
        if (value == null) return null;

        var builder = Nodes.CastParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getDataType();
        if (v1 != null) builder.setDataType(convert(v1));
        var v2 = value.getMaxLength();
        if (v2 != null) builder.setMaxLength(v2);
        var v3 = value.getScale();
        if (v3 != null) builder.setScale(v3);

        return builder;
    }

    public static Nodes.CursorName.Builder convert(CursorName value) {
        if (value == null) return null;

        var builder = Nodes.CursorName.newBuilder();

        var v0 = value.getName();
        if (v0 != null) builder.setName(v0);
        builder.setIsCaseSensitive(value.isCaseSensitive());

        return builder;
    }

    public static Nodes.CreateIndexStatement.Builder convert(CreateIndexStatement value) {
        if (value == null) return null;

        var builder = Nodes.CreateIndexStatement.newBuilder();

        builder.setBindCount(value.getBindCount());
        var v0 = value.getOperation();
        if (v0 != null) builder.setOperation(convert(v0));
        var v1 = value.getTable();
        if (v1 != null) builder.setTable(convert(v1));
        addAll(value.getIncludeColumns(), NodeConverter::convert, builder::addIncludeColumns);
        var v2 = value.getIndexConstraint();
        if (v2 != null) builder.setIndexConstraint(convert(v2));
        var v3 = value.getIndexTableName();
        if (v3 != null) builder.setIndexTableName(convert(v3));
        var v4 = value.getIndexType();
        if (v4 != null) builder.setIndexType(convert(v4));
        addAll(value.getSplitNodes(), NodeConverter::convert, builder::addSplitNodes);
        builder.setIfNotExists(value.ifNotExists());
        builder.setIsAsync(value.isAsync());

        return builder;
    }

    public static Nodes.MultiplyParseNode.Builder convert(MultiplyParseNode value) {
        if (value == null) return null;

        var builder = Nodes.MultiplyParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getOperator();
        if (v1 != null) builder.setOperator(v1);

        return builder;
    }

    public static Nodes.ArrayAnyComparisonNode.Builder convert(ArrayAnyComparisonNode value) {
        if (value == null) return null;

        var builder = Nodes.ArrayAnyComparisonNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getType();
        if (v1 != null) builder.setType(v1);

        return builder;
    }

    public static Nodes.P_SingleTableStatement.Builder convert(SingleTableStatement value) {
        if (value == null) return null;

        var builder = Nodes.P_SingleTableStatement.newBuilder();

        if (value instanceof DeleteStatement) {
            builder.setDeleteStatement(convert((DeleteStatement) value));
        } else if (value instanceof UpsertStatement) {
            builder.setUpsertStatement(convert((UpsertStatement) value));
        } else if (value instanceof UpdateStatisticsStatement) {
            builder.setUpdateStatisticsStatement(convert((UpdateStatisticsStatement) value));
        } else if (value instanceof AlterIndexStatement) {
            builder.setAlterIndexStatement(convert((AlterIndexStatement) value));
        } else if (value instanceof DropColumnStatement) {
            builder.setDropColumnStatement(convert((DropColumnStatement) value));
        } else if (value instanceof AddColumnStatement) {
            builder.setAddColumnStatement(convert((AddColumnStatement) value));
        } else if (value instanceof CreateIndexStatement) {
            builder.setCreateIndexStatement(convert((CreateIndexStatement) value));
        } else if (value instanceof DMLStatement) {
            builder.setDMLStatement(convertDefault((DMLStatement) value));
        }

        return builder;
    }

    public static Nodes.LimitNode.Builder convert(LimitNode value) {
        if (value == null) return null;

        var builder = Nodes.LimitNode.newBuilder();

        var v0 = value.getLimitParseNode();
        if (v0 != null) builder.setLimitParseNode(convert(v0));

        return builder;
    }

    public static Nodes.ToTimestampParseNode.Builder convert(ToTimestampParseNode value) {
        if (value == null) return null;

        var builder = Nodes.ToTimestampParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getName();
        if (v1 != null) builder.setName(v1);
        builder.setIsAggregate(value.isAggregate());

        return builder;
    }

    public static Nodes.AddParseNode.Builder convert(AddParseNode value) {
        if (value == null) return null;

        var builder = Nodes.AddParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getOperator();
        if (v1 != null) builder.setOperator(v1);

        return builder;
    }

    public static Nodes.NamedTableNode.Builder convert(NamedTableNode value) {
        if (value == null) return null;

        var builder = Nodes.NamedTableNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        var v1 = value.getName();
        if (v1 != null) builder.setName(convert(v1));
        var v2 = value.getTableSamplingRate();
        if (v2 != null) builder.setTableSamplingRate(v2);
        addAll(value.getDynamicColumns(), NodeConverter::convert, builder::addDynamicColumns);

        return builder;
    }

    public static Nodes.ArrayConstructorNode.Builder convert(ArrayConstructorNode value) {
        if (value == null) return null;

        var builder = Nodes.ArrayConstructorNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());

        return builder;
    }

    public static Nodes.FamilyWildcardParseNode.Builder convert(FamilyWildcardParseNode value) {
        if (value == null) return null;

        var builder = Nodes.FamilyWildcardParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getName();
        if (v1 != null) builder.setName(v1);
        builder.setIsCaseSensitive(value.isCaseSensitive());
        builder.setIsRewrite(value.isRewrite());

        return builder;
    }

    public static Nodes.UpsertStatement.Pair_ColumnName_ParseNode.Builder convertPairColumnNameParseNode(Pair<ColumnName, ParseNode> value) {
        if (value == null) return null;

        var builder = Nodes.UpsertStatement.Pair_ColumnName_ParseNode.newBuilder();

        builder.setFirst(convert(value.getFirst()));
        builder.setSecond(convert(value.getSecond()));


        return builder;
    }

    public static Nodes.UpsertStatement.Builder convert(UpsertStatement value) {
        if (value == null) return null;

        var builder = Nodes.UpsertStatement.newBuilder();

        builder.setBindCount(value.getBindCount());
        var v0 = value.getOperation();
        if (v0 != null) builder.setOperation(convert(v0));
        var v1 = value.getTable();
        if (v1 != null) builder.setTable(convert(v1));
        addAll(value.getColumns(), NodeConverter::convert, builder::addColumns);
        var v2 = value.getHint();
        if (v2 != null && !v2.isEmpty()) builder.setHint(convert(v2));
        var v3 = value.getOnDupKeyPairs();
        if (v3 != null) {
            if (v3.size() == 0) {
                builder.setOnDupKeyIgnore(true);
            } else {
                addAll(v3, NodeConverter::convertPairColumnNameParseNode, builder::addOnDupKeyPairs);
            }
        }
        var v4 = value.getSelect();
        if (v4 != null) builder.setSelect(convert(v4));
        addAll(value.getValues(), NodeConverter::convert, builder::addValues);


        return builder;
    }

    public static Nodes.LastValueAggregateParseNode.Builder convert(LastValueAggregateParseNode value) {
        if (value == null) return null;

        var builder = Nodes.LastValueAggregateParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getName();
        if (v1 != null) builder.setName(v1);
        builder.setIsAggregate(value.isAggregate());

        return builder;
    }

    public static Nodes.P_TableNode.Builder convert(TableNode value) {
        if (value == null) return null;

        var builder = Nodes.P_TableNode.newBuilder();

        if (value instanceof JoinTableNode) {
            builder.setJoinTableNode(convert((JoinTableNode) value));
        } else if (value instanceof DerivedTableNode) {
            builder.setDerivedTableNode(convert((DerivedTableNode) value));
        } else if (value instanceof BindTableNode) {
            builder.setBindTableNode(convert((BindTableNode) value));
        } else if (value instanceof NamedTableNode) {
            builder.setNamedTableNode(convert((NamedTableNode) value));
        }

        return builder;
    }

    public static Nodes.ExecuteUpgradeStatement.Builder convert(ExecuteUpgradeStatement value) {
        if (value == null) return null;

        var builder = Nodes.ExecuteUpgradeStatement.newBuilder();

        builder.setBindCount(value.getBindCount());
        var v0 = value.getOperation();
        if (v0 != null) builder.setOperation(convert(v0));

        return builder;
    }

    public static Nodes.DistinctCountHyperLogLogAggregateParseNode.Builder convert(DistinctCountHyperLogLogAggregateParseNode value) {
        if (value == null) return null;

        var builder = Nodes.DistinctCountHyperLogLogAggregateParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getName();
        if (v1 != null) builder.setName(v1);
        builder.setIsAggregate(value.isAggregate());

        return builder;
    }

    public static Nodes.P_ConcreteTableNode.Builder convert(ConcreteTableNode value) {
        if (value == null) return null;

        var builder = Nodes.P_ConcreteTableNode.newBuilder();

        if (value instanceof BindTableNode) {
            builder.setBindTableNode(convert((BindTableNode) value));
        } else if (value instanceof NamedTableNode) {
            builder.setNamedTableNode(convert((NamedTableNode) value));
        }

        return builder;
    }

    public static Nodes.OffsetNode.Builder convert(OffsetNode value) {
        if (value == null) return null;

        var builder = Nodes.OffsetNode.newBuilder();

        var v0 = value.getOffsetParseNode();
        if (v0 != null) builder.setOffsetParseNode(convert(v0));

        return builder;
    }

    public static Nodes.AvgAggregateParseNode.Builder convert(AvgAggregateParseNode value) {
        if (value == null) return null;

        var builder = Nodes.AvgAggregateParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getName();
        if (v1 != null) builder.setName(v1);
        builder.setIsAggregate(value.isAggregate());

        return builder;
    }

    public static Nodes.Action convert(Permission.Action value) {
        if (value == Permission.Action.READ) {
            return Nodes.Action.READ;
        } else if (value == Permission.Action.WRITE) {
            return Nodes.Action.WRITE;
        } else if (value == Permission.Action.EXEC) {
            return Nodes.Action.EXEC;
        } else if (value == Permission.Action.CREATE) {
            return Nodes.Action.CREATE;
        }

        return Nodes.Action.ADMIN;
    }

    public static Nodes.ColumnName.Builder convert(ColumnName value) {
        if (value == null) return null;

        var builder = Nodes.ColumnName.newBuilder();

        var v0 = value.getColumnNode();
        if (v0 != null) builder.setColumnNode(convert(v0));
        var v1 = value.getFamilyNode();
        if (v1 != null) builder.setFamilyNode(convert(v1));

        return builder;
    }

    public static Nodes.P_ArithmeticParseNode.Builder convert(ArithmeticParseNode value) {
        if (value == null) return null;

        var builder = Nodes.P_ArithmeticParseNode.newBuilder();

        if (value instanceof SubtractParseNode) {
            builder.setSubtractParseNode(convert((SubtractParseNode) value));
        } else if (value instanceof ModulusParseNode) {
            builder.setModulusParseNode(convert((ModulusParseNode) value));
        } else if (value instanceof MultiplyParseNode) {
            builder.setMultiplyParseNode(convert((MultiplyParseNode) value));
        } else if (value instanceof AddParseNode) {
            builder.setAddParseNode(convert((AddParseNode) value));
        } else if (value instanceof DivideParseNode) {
            builder.setDivideParseNode(convert((DivideParseNode) value));
        }

        return builder;
    }

    public static Nodes.SubqueryParseNode.Builder convert(SubqueryParseNode value) {
        if (value == null) return null;

        var builder = Nodes.SubqueryParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        builder.setExpectSingleRow(value.expectSingleRow());
        var v1 = value.getSelectNode();
        if (v1 != null) builder.setSelectNode(convert(v1));

        return builder;
    }

    public static Nodes.P_TerminalParseNode.Builder convert(TerminalParseNode value) {
        if (value == null) return null;

        var builder = Nodes.P_TerminalParseNode.newBuilder();

        if (value instanceof ColumnParseNode) {
            builder.setColumnParseNode(convert((ColumnParseNode) value));
        } else if (value instanceof TableWildcardParseNode) {
            builder.setTableWildcardParseNode(convert((TableWildcardParseNode) value));
        } else if (value instanceof BindParseNode) {
            builder.setBindParseNode(convert((BindParseNode) value));
        } else if (value instanceof FamilyWildcardParseNode) {
            builder.setFamilyWildcardParseNode(convert((FamilyWildcardParseNode) value));
        } else if (value instanceof SequenceValueParseNode) {
            builder.setSequenceValueParseNode(convert((SequenceValueParseNode) value));
        } else if (value instanceof SubqueryParseNode) {
            builder.setSubqueryParseNode(convert((SubqueryParseNode) value));
        } else if (value instanceof LiteralParseNode) {
            builder.setLiteralParseNode(convert((LiteralParseNode) value));
        } else if (value instanceof WildcardParseNode) {
            builder.setWildcardParseNode(convert((WildcardParseNode) value));
        }

        return builder;
    }

    public static Nodes.FunctionArgument.Builder convert(PFunction.FunctionArgument value) {
        if (value == null) return null;

        var builder = Nodes.FunctionArgument.newBuilder();

        builder.setArgPosition(value.getArgPosition());
        var v0 = value.getArgumentType();
        if (v0 != null) builder.setArgumentType(v0);
        var v1 = value.getDefaultValue();
        if (v1 != null) builder.setDefaultValue(convert(v1));
        var v2 = value.getMaxValue();
        if (v2 != null) builder.setMaxValue(convert(v2));
        var v3 = value.getMinValue();
        if (v3 != null) builder.setMinValue(convert(v3));
        builder.setIsArrayType(value.isArrayType());
        builder.setIsConstant(value.isConstant());

        return builder;
    }

    public static Nodes.MaxAggregateParseNode.Builder convert(MaxAggregateParseNode value) {
        if (value == null) return null;

        var builder = Nodes.MaxAggregateParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getName();
        if (v1 != null) builder.setName(v1);
        builder.setIsAggregate(value.isAggregate());

        return builder;
    }

    public static Nodes.DropSchemaStatement.Builder convert(DropSchemaStatement value) {
        if (value == null) return null;

        var builder = Nodes.DropSchemaStatement.newBuilder();

        builder.setBindCount(value.getBindCount());
        var v0 = value.getOperation();
        if (v0 != null) builder.setOperation(convert(v0));
        builder.setCascade(value.cascade());
        var v1 = value.getSchemaName();
        if (v1 != null) builder.setSchemaName(v1);
        builder.setIfExists(value.ifExists());

        return builder;
    }

    public static Nodes.P_DelegateConstantToCountParseNode.Builder convert(DelegateConstantToCountParseNode value) {
        if (value == null) return null;

        var builder = Nodes.P_DelegateConstantToCountParseNode.newBuilder();

        if (value instanceof FirstValuesAggregateParseNode) {
            builder.setFirstValuesAggregateParseNode(convert((FirstValuesAggregateParseNode) value));
        } else if (value instanceof NthValueAggregateParseNode) {
            builder.setNthValueAggregateParseNode(convert((NthValueAggregateParseNode) value));
        } else if (value instanceof MinAggregateParseNode) {
            builder.setMinAggregateParseNode(convert((MinAggregateParseNode) value));
        } else if (value instanceof DistinctCountParseNode) {
            builder.setDistinctCountParseNode(convert((DistinctCountParseNode) value));
        } else if (value instanceof LastValuesAggregateParseNode) {
            builder.setLastValuesAggregateParseNode(convert((LastValuesAggregateParseNode) value));
        } else if (value instanceof SumAggregateParseNode) {
            builder.setSumAggregateParseNode(convert((SumAggregateParseNode) value));
        } else if (value instanceof FirstValueAggregateParseNode) {
            builder.setFirstValueAggregateParseNode(convert((FirstValueAggregateParseNode) value));
        } else if (value instanceof LastValueAggregateParseNode) {
            builder.setLastValueAggregateParseNode(convert((LastValueAggregateParseNode) value));
        } else if (value instanceof DistinctCountHyperLogLogAggregateParseNode) {
            builder.setDistinctCountHyperLogLogAggregateParseNode(convert((DistinctCountHyperLogLogAggregateParseNode) value));
        } else if (value instanceof MaxAggregateParseNode) {
            builder.setMaxAggregateParseNode(convert((MaxAggregateParseNode) value));
        }

        return builder;
    }

    public static Nodes.AddJarsStatement.Builder convert(AddJarsStatement value) {
        if (value == null) return null;

        var builder = Nodes.AddJarsStatement.newBuilder();

        builder.setBindCount(value.getBindCount());
        var v0 = value.getOperation();
        if (v0 != null) builder.setOperation(convert(v0));
        addAll(value.getJarPaths(), NodeConverter::convert, builder::addJarPaths);

        return builder;
    }

    public static Nodes.CreateSchemaStatement.Builder convert(CreateSchemaStatement value) {
        if (value == null) return null;

        var builder = Nodes.CreateSchemaStatement.newBuilder();

        builder.setBindCount(value.getBindCount());
        var v0 = value.getOperation();
        if (v0 != null) builder.setOperation(convert(v0));
        var v1 = value.getSchemaName();
        if (v1 != null) builder.setSchemaName(v1);
        builder.setIsIfNotExists(value.isIfNotExists());

        return builder;
    }

    public static Nodes.ExistsParseNode.Builder convert(ExistsParseNode value) {
        if (value == null) return null;

        var builder = Nodes.ExistsParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        builder.setIsNegate(value.isNegate());

        return builder;
    }

    public static Nodes.ToCharParseNode.Builder convert(ToCharParseNode value) {
        if (value == null) return null;

        var builder = Nodes.ToCharParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getName();
        if (v1 != null) builder.setName(v1);
        builder.setIsAggregate(value.isAggregate());

        return builder;
    }

    public static Nodes.TableName.Builder convert(TableName value) {
        if (value == null) return null;

        var builder = Nodes.TableName.newBuilder();

        var v0 = value.getSchemaName();
        if (v0 != null) builder.setSchemaName(v0);
        var v1 = value.getTableName();
        if (v1 != null) builder.setTableName(v1);
        builder.setIsSchemaNameCaseSensitive(value.isSchemaNameCaseSensitive());
        builder.setIsTableNameCaseSensitive(value.isTableNameCaseSensitive());

        return builder;
    }

    public static Nodes.CloseStatement.Builder convert(CloseStatement value) {
        if (value == null) return null;

        var builder = Nodes.CloseStatement.newBuilder();

        builder.setBindCount(value.getBindCount());
        var v0 = value.getOperation();
        if (v0 != null) builder.setOperation(convert(v0));
        var v1 = value.getCursorName();
        if (v1 != null) builder.setCursorName(v1);

        return builder;
    }

    public static Nodes.ArrayAllComparisonNode.Builder convert(ArrayAllComparisonNode value) {
        if (value == null) return null;

        var builder = Nodes.ArrayAllComparisonNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getType();
        if (v1 != null) builder.setType(v1);

        return builder;
    }

    public static Nodes.LiteralParseNode.Builder convert(LiteralParseNode value) {
        if (value == null) return null;

        var builder = Nodes.LiteralParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getType();
        if (v1 != null) builder.setType(convert(v1));
        var v2 = new StringBuilder();
        value.toSQL(null, v2);
        builder.setValue(v2.toString());

        return builder;
    }

    public static Nodes.WildcardParseNode.Builder convert(WildcardParseNode value) {
        if (value == null) return null;

        var builder = Nodes.WildcardParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        builder.setIsRewrite(value.isRewrite());

        return builder;
    }

    public static Nodes.StringConcatParseNode.Builder convert(StringConcatParseNode value) {
        if (value == null) return null;

        var builder = Nodes.StringConcatParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());

        return builder;
    }

    public static Nodes.AddColumnStatement.Builder convert(AddColumnStatement value) {
        if (value == null) return null;

        var builder = Nodes.AddColumnStatement.newBuilder();

        builder.setBindCount(value.getBindCount());
        var v0 = value.getOperation();
        if (v0 != null) builder.setOperation(convert(v0));
        var v1 = value.getTable();
        if (v1 != null) builder.setTable(convert(v1));
        var v2 = value.getTableType();
        if (v2 != null) builder.setTableType(convert(v2));
        addAll(value.getColumnDefs(), NodeConverter::convert, builder::addColumnDefs);
        builder.setIfNotExists(value.ifNotExists());

        return builder;
    }

    public static Nodes.DivideParseNode.Builder convert(DivideParseNode value) {
        if (value == null) return null;

        var builder = Nodes.DivideParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());
        var v1 = value.getOperator();
        if (v1 != null) builder.setOperator(v1);

        return builder;
    }

    public static Nodes.CaseParseNode.Builder convert(CaseParseNode value) {
        if (value == null) return null;

        var builder = Nodes.CaseParseNode.newBuilder();

        var v0 = value.getAlias();
        if (v0 != null) builder.setAlias(v0);
        addAll(value.getChildren(), NodeConverter::convert, builder::addChildren);
        builder.setIsStateless(value.isStateless());

        return builder;
    }

    private static String convert(LiteralExpression value) {
        return value.toString();
    }

    private static Nodes.PDataType convert(PDataType<?> value) {
        if (value instanceof PUnsignedDoubleArray) {
            return Nodes.PDataType.UnsignedDoubleArray;
        } else if (value instanceof PUnsignedFloatArray) {
            return Nodes.PDataType.UnsignedFloatArray;
        } else if (value instanceof PUnsignedTinyintArray) {
            return Nodes.PDataType.UnsignedTinyintArray;
        } else if (value instanceof PUnsignedSmallintArray) {
            return Nodes.PDataType.UnsignedSmallintArray;
        } else if (value instanceof PUnsignedIntArray) {
            return Nodes.PDataType.UnsignedIntArray;
        } else if (value instanceof PUnsignedLongArray) {
            return Nodes.PDataType.UnsignedLongArray;
        } else if (value instanceof PUnsignedDateArray) {
            return Nodes.PDataType.UnsignedDateArray;
        } else if (value instanceof PDateArray) {
            return Nodes.PDataType.DateArray;
        } else if (value instanceof PUnsignedTimeArray) {
            return Nodes.PDataType.UnsignedTimeArray;
        } else if (value instanceof PTimeArray) {
            return Nodes.PDataType.TimeArray;
        } else if (value instanceof PUnsignedTimestampArray) {
            return Nodes.PDataType.UnsignedTimestampArray;
        } else if (value instanceof PTimestampArray) {
            return Nodes.PDataType.TimestampArray;
        } else if (value instanceof PDecimalArray) {
            return Nodes.PDataType.DecimalArray;
        } else if (value instanceof PDoubleArray) {
            return Nodes.PDataType.DoubleArray;
        } else if (value instanceof PFloatArray) {
            return Nodes.PDataType.FloatArray;
        } else if (value instanceof PTinyintArray) {
            return Nodes.PDataType.TinyintArray;
        } else if (value instanceof PSmallintArray) {
            return Nodes.PDataType.SmallintArray;
        } else if (value instanceof PLongArray) {
            return Nodes.PDataType.LongArray;
        } else if (value instanceof PCharArray) {
            return Nodes.PDataType.CharArray;
        } else if (value instanceof PBinaryArray) {
            return Nodes.PDataType.BinaryArray;
        } else if (value instanceof PVarbinaryArray) {
            return Nodes.PDataType.VarbinaryArray;
        } else if (value instanceof PVarcharArray) {
            return Nodes.PDataType.VarcharArray;
        } else if (value instanceof PBooleanArray) {
            return Nodes.PDataType.BooleanArray;
        } else if (value instanceof PIntegerArray) {
            return Nodes.PDataType.IntegerArray;
        } else if (value instanceof PBinary) {
            return Nodes.PDataType.Binary;
        } else if (value instanceof PVarbinary) {
            return Nodes.PDataType.Varbinary;
        } else if (value instanceof PBoolean) {
            return Nodes.PDataType.Boolean;
        } else if (value instanceof PUnsignedDouble) {
            return Nodes.PDataType.UnsignedDouble;
        } else if (value instanceof PUnsignedFloat) {
            return Nodes.PDataType.UnsignedFloat;
        } else if (value instanceof PUnsignedTinyint) {
            return Nodes.PDataType.UnsignedTinyint;
        } else if (value instanceof PUnsignedSmallint) {
            return Nodes.PDataType.UnsignedSmallint;
        } else if (value instanceof PUnsignedInt) {
            return Nodes.PDataType.UnsignedInt;
        } else if (value instanceof PUnsignedLong) {
            return Nodes.PDataType.UnsignedLong;
        } else if (value instanceof PUnsignedDate) {
            return Nodes.PDataType.UnsignedDate;
        } else if (value instanceof PUnsignedTime) {
            return Nodes.PDataType.UnsignedTime;
        } else if (value instanceof PUnsignedTimestamp) {
            return Nodes.PDataType.UnsignedTimestamp;
        } else if (value instanceof PDate) {
            return Nodes.PDataType.Date;
        } else if (value instanceof PTime) {
            return Nodes.PDataType.Time;
        } else if (value instanceof PTimestamp) {
            return Nodes.PDataType.Timestamp;
        } else if (value instanceof PDecimal) {
            return Nodes.PDataType.Decimal;
        } else if (value instanceof PDouble) {
            return Nodes.PDataType.Double;
        } else if (value instanceof PFloat) {
            return Nodes.PDataType.Float;
        } else if (value instanceof PTinyint) {
            return Nodes.PDataType.Tinyint;
        } else if (value instanceof PSmallint) {
            return Nodes.PDataType.Smallint;
        } else if (value instanceof PInteger) {
            return Nodes.PDataType.Integer;
        } else if (value instanceof PLong) {
            return Nodes.PDataType.Long;
        } else if (value instanceof PChar) {
            return Nodes.PDataType.Char;
        }

        return Nodes.PDataType.Varchar;

    }

    private static Nodes.PName.Builder convert(PName value) {
        var builder = Nodes.PName.newBuilder();

        var v0 = value.getString();
        if (v0 != null) builder.setValue(v0);

        return builder;
    }

    private static String convert(Object value) {
        return gson.toJson(value);
    }

    private static <TNode, TProto> void addAll(List<TNode> source, Function<TNode, TProto> convert, Consumer<TProto> add) {
        if (source == null || source.isEmpty()) {
            return;
        }

        for (final var node : source) {
            add.accept(convert.apply(node));
        }
    }

    private static <TNode, TProto> void addAll(TNode[] source, Function<TNode, TProto> convert, Consumer<TProto> add) {
        if (source == null || source.length == 0) {
            return;
        }

        for (final var node : source) {
            add.accept(convert.apply(node));
        }
    }
}
