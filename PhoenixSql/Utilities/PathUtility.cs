using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices;
using J2NET.Exceptions;

namespace PhoenixSql.Utilities
{
    internal static class PathUtility
    {
        public static string GetJavaRuntimePath()
        {
            string architecture = null;

            if (RuntimeInformation.IsOSPlatform(OSPlatform.Windows))
            {
                switch (RuntimeInformation.OSArchitecture)
                {
                    case Architecture.X86:
                        architecture = "win-x86";
                        break;

                    case Architecture.X64:
                        architecture = "win-x64";
                        break;
                }
            }
            else if (RuntimeInformation.IsOSPlatform(OSPlatform.Linux))
            {
                architecture = "linux";
            }
            else if (RuntimeInformation.IsOSPlatform(OSPlatform.OSX))
            {
                architecture = "mac";
            }

            if (string.IsNullOrEmpty(architecture))
                throw new PlatformNotSupportedException();

            var path = Find(Path.Combine("runtimes", architecture, "bin"));

            if (string.IsNullOrEmpty(path))
                throw new RuntimeNotFoundException();

            return Path.Combine(path, "java");
        }

        public static string Find(string path)
        {
            return GetSearchPaths()
                .Select(d => Path.GetFullPath(Path.Combine(d, path)))
                .FirstOrDefault(p => File.Exists(p) || Directory.Exists(p));
        }

        private static IEnumerable<string> GetSearchPaths()
        {
            yield return Environment.CurrentDirectory;
            yield return AppContext.BaseDirectory;
        }
    }
}
