using System;
using System.Collections.Generic;
using System.Reflection;

namespace PhoenixSql.Sample
{
    internal static class Program
    {
        private static void Main(string[] args)
        {
            Console.WriteLine(typeof(List<int>).BaseType);
        }
    }
}