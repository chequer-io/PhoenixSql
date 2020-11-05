using System;
using System.Diagnostics;

namespace PhoenixSql.Sample
{
    internal static class Program
    {
        private static void Main(string[] args)
        {
            while (true)
            {
                var sw = Stopwatch.StartNew();

                var s = PhoenixSqlParser.Parse("select 1");

                sw.Stop();
                Console.WriteLine($"parsed in {sw.Elapsed.TotalMilliseconds:0.00} ms");
                break;
            }
        }
    }
}
