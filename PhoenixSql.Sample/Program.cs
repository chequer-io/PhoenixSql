﻿using System;
using System.Collections;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Reflection;
using System.Threading;
using Google.Protobuf;
using Google.Protobuf.Collections;
using PhoenixSql.Extensions;

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

                    try
                    {
                        Console.ForegroundColor = ConsoleColor.DarkCyan;
                        Console.WriteLine($"Deparse : {PhoenixSqlDeparser.Deparse(statement)}");
                    }
                    catch (Exception e)
                    {
                        Console.WriteLine(e);
                    }

                    Console.ForegroundColor = ConsoleColor.Magenta;
                    Console.WriteLine($"Parsed in {sw.Elapsed.TotalMilliseconds:0.00} ms");

                    Console.WriteLine();

                    Print(null, statement);
                }
                catch (Exception e)
                {
                    Console.ForegroundColor = ConsoleColor.Red;
                    Console.WriteLine(e);
                }

                Console.WriteLine();
            }
        }

        private static void Print(string key, object v, int depth = 0)
        {
            if (v is IPhoenixNode node)
                v = node.Unwrap();

            if (depth > 0)
                Console.Write(new string(' ', depth * 4));

            if (key != null)
            {
                Console.ForegroundColor = ConsoleColor.DarkGray;
                Console.Write($"{key}: ");
            }

            if (v is IMessage)
            {
                Console.ForegroundColor = ConsoleColor.Green;
                Console.WriteLine(v.GetType().Name);

                foreach (var propertyInfo in v.GetType().GetProperties().Where(p => !p.GetMethod!.IsStatic))
                {
                    if (propertyInfo.DeclaringType.IsInterface)
                        continue;

                    var value = propertyInfo.GetValue(v);

                    if (!(value is string) && value is IEnumerable<IPhoenixNode> enumerable)
                    {
                        int index = 0;

                        foreach (var child in enumerable)
                        {
                            Print($"{propertyInfo.Name}[{index++}]", child, depth + 1);
                        }

                        continue;
                    }

                    if (propertyInfo.PropertyType.Assembly == typeof(IMessage).Assembly)
                    {
                        continue;
                    }

                    switch (value)
                    {
                        case null:
                        case string strValue when string.IsNullOrEmpty(strValue):
                        case bool boolValue when !boolValue:
                        case int intValue when intValue == 0:
                        case double doubleValue when Math.Abs(doubleValue) < double.Epsilon:
                            continue;

                        default:
                            Print(propertyInfo.Name, value, depth + 1);
                            break;
                    }
                }
            }
            else
            {
                Console.ForegroundColor = ConsoleColor.Red;
                Console.WriteLine(v);
            }
        }
    }
}
