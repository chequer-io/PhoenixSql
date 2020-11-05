using System;
using System.Diagnostics;
using System.Threading;

namespace PhoenixSql.Sample
{
    internal static class Program
    {
        private static void Main(string[] args)
        {
            var defaultColor = Console.ForegroundColor;

            while (true)
            {
                try
                {
                    Console.ForegroundColor = defaultColor;
                    Console.Write("SQL: ");

                    Console.ForegroundColor = ConsoleColor.Cyan;
                    var sql = Console.ReadLine();

                    var sw = Stopwatch.StartNew();
                    var statement = PhoenixSqlParser.Parse(sql);
                    sw.Stop();

                    Print(statement);

                    Console.WriteLine($"parsed in {sw.Elapsed.TotalMilliseconds:0.00} ms");
                }
                catch (Exception e)
                {
                    Console.WriteLine(e);
                }

                Console.WriteLine();
            }
        }

        private static void Print(IBindableStatement statement, int depth = 0)
        {
            if (depth > 0)
                Console.Write(new string(' ', depth * 4));

            Console.WriteLine(statement.GetType().Name);
        }
    }
}
