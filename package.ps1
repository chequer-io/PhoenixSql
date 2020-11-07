dotnet build PhoenixSql -c Release --no-incremental
dotnet pack PhoenixSql -c Release -p:Packaging=true -o build
