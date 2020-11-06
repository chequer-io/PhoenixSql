<img width="128" src="https://github.com/chequer-io/phoenixsql/blob/main/Logo.png?raw=true">

# PhoenixSql .NET Core Library

## Overview
[![Nuget](https://img.shields.io/nuget/v/PhoenixSql)](https://www.nuget.org/packages/PhoenixSql/)


Phoenix sql parser for .NET Core using [phoenix-core](https://mvnrepository.com/artifact/org.apache.phoenix/phoenix-core) library.

## Installation

### 1. Install NuGet pacakge
Install the latest version of the [PhoenixSql](https://www.nuget.org/packages/CUBRID.Data/) package from NuGet.

### 2. Add J2NET Runtime package reference
Paste the following XML into your Project(*.csproj / .vbproj / .fsproj*) file.

```xml
<PropertyGroup>
    <RuntimeVersion>1.1.0</RuntimeVersion>
    <OSPlatform Condition="'$([System.Runtime.InteropServices.RuntimeInformation]::IsOSPlatform($([System.Runtime.InteropServices.OSPlatform]::OSX)))' == 'true'">OSX</OSPlatform>
    <OSPlatform Condition="'$([System.Runtime.InteropServices.RuntimeInformation]::IsOSPlatform($([System.Runtime.InteropServices.OSPlatform]::Linux)))' == 'true'">Linux</OSPlatform>
    <OSPlatform Condition="'$([System.Runtime.InteropServices.RuntimeInformation]::IsOSPlatform($([System.Runtime.InteropServices.OSPlatform]::Windows)))' == 'true'">Windows</OSPlatform>
    <OSArchitecture>$([System.Runtime.InteropServices.RuntimeInformation]::OSArchitecture)</OSArchitecture>
</PropertyGroup>

<ItemGroup Label="Development" Condition=" '$(RID)' == '' And '$(Packaging)' == '' ">
    <PackageReference Condition=" '$(OSPlatform)' == 'OSX' And '$(OSArchitecture)' == 'X64' " Include="J2NET.Runtime.Mac" Version="$(RuntimeVersion)" />
    <PackageReference Condition=" '$(OSPlatform)' == 'Linux' And '$(OSArchitecture)' == 'X64' " Include="J2NET.Runtime.Linux" Version="$(RuntimeVersion)" />
    <PackageReference Condition=" '$(OSPlatform)' == 'Windows' And '$(OSArchitecture)' == 'X64' " Include="J2NET.Runtime.Win64" Version="$(RuntimeVersion)" />
    <PackageReference Condition=" '$(OSPlatform)' == 'Windows' And '$(OSArchitecture)' == 'X86' " Include="J2NET.Runtime.Win32" Version="$(RuntimeVersion)" />
</ItemGroup>
```

### 3. Parse SQL
```csharp
var sql = "SELECT * FROM table";
var statement = PhoenixSqlParser.Parse(sql);
```
