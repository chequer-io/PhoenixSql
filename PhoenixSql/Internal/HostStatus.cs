namespace PhoenixSql.Internal
{
    internal enum HostStatus
    {
        WatingToConnect,
        WatingToHandshaking,
        Handshaking,
        Connecting,
        Connected,
        Closed
    }
}
