Param (
    [Parameter(Mandatory = $true)]
    [Version] $Version,

    [Parameter(Mandatory = $true)]
    [string] $NugetApiKey
)

Function Get-Nuget-Package-Version {
    Param (
        [Parameter(Mandatory = $true)][string] $PackageName
    )

    return [Version](Invoke-WebRequest https://api.nuget.org/v3-flatcontainer/$PackageName/index.json | ConvertFrom-Json).versions[-1]
}

# Clean .nupkg
if (Test-Path build) {
    Remove-Item build -Recurse -Force
}

# Clean .jar
$jarPath = "./PhoenixSql.Host/target/PhoenixSql.Host-1.0-SNAPSHOT-jar-with-dependencies.jar"

if (Test-Path $jarPath) {
    Remove-Item $jarPath
}

# Build Java
Push-Location "./PhoenixSql.Host"

mvn --settings settings.xml clean package

Pop-Location

# Build .NET
dotnet build PhoenixSql -c Release --no-incremental
dotnet pack PhoenixSql -c Release -p:Version=$Version -p:Packaging=true -o build

# Publish
dotnet nuget push "./build/PhoenixSql.$Version.nupkg" --source "https://api.nuget.org/v3/index.json" --api-key $NugetApiKey

# Waiting for indexing
Write-Host "Waiting for indexing .."

while ($true) {
    $PackageVersion = Get-Nuget-Package-Version "PhoenixSql"

    Write-Host "Current version is $PackageVersion"

    if ($PackageVersion -eq $Version) {
        Write-Host "NuGet package PhoenixSql $Version has been indexed"
        break
    }

    sleep 15
}
