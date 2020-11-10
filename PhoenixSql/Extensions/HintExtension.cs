using System;

namespace PhoenixSql.Extensions
{
    public static class HintExtension
    {
        public static string ToSql(this Hint hint)
        {
            return hint switch
            {
                Hint.RangeScan => "RANGE_SCAN",
                Hint.SkipScan => "SKIP_SCAN",
                Hint.NoChildParentJoinOptimization => "NO_CHILD_PARENT_JOIN_OPTIMIZATION",
                Hint.NoIndex => "NO_INDEX",
                Hint.Index1 => "INDEX",
                Hint.UseDataOverIndexTable => "USE_DATA_OVER_INDEX_TABLE",
                Hint.UseIndexOverDataTable => "USE_INDEX_OVER_DATA_TABLE",
                Hint.NoCache => "NO_CACHE",
                Hint.UseSortMergeJoin => "USE_SORT_MERGE_JOIN",
                Hint.NoStarJoin => "NO_STAR_JOIN",
                Hint.SeekToColumn => "SEEK_TO_COLUMN",
                Hint.NoSeekToColumn => "NO_SEEK_TO_COLUMN",
                Hint.Small => "SMALL",
                Hint.Serial => "SERIAL",
                Hint.ForwardScan => "FORWARD_SCAN",
                _ => throw new ArgumentOutOfRangeException()
            };
        }
    }
}
