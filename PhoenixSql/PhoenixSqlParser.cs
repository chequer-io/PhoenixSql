using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using J2NET;
using J2NET.Exceptions;
using J2NET.Utilities;

namespace PhoenixSql
{
    public sealed class PhoenixSqlParser
    {
        public static void Test()
        {
            var jar = @"C:\Users\devel\Desktop\Work\PhoenixSql\PhoenixSql.Host\target\PhoenixSql.Host-1.0-SNAPSHOT-jar-with-dependencies.jar";
            var runtimePath = PathUtility.GetRuntimePath();

            if (!Directory.Exists(Path.GetDirectoryName(runtimePath)))
                throw new RuntimeNotFoundException();

            var p = Process.Start(new ProcessStartInfo
            {
                FileName = runtimePath,
                Arguments = $"-jar {jar}",
                UseShellExecute = false,
                RedirectStandardOutput = true,
                CreateNoWindow = true
            });

            var d = p.StandardOutput.ReadToEnd();
        }

        public static IEnumerable<IBindableStatement> Parse(string sql)
        {
            throw new NotImplementedException();
        }
    }
}
