namespace PhoenixSql
{
    public interface IDMLStatement : ISingleTableStatement
    {
        Google.Protobuf.Collections.RepeatedField<UDFMapEntry> UdfParseNodes { get; }
    }
}
