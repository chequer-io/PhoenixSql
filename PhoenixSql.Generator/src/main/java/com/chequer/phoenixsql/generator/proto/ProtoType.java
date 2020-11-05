package com.chequer.phoenixsql.generator.proto;

public class ProtoType {
    private String name;
    private String importFile;
    private final boolean isScalarType;

    public ProtoType(ProtoScalaType scalaType) {
        name = scalaType.value;
        importFile = null;
        isScalarType = true;
    }

    public ProtoType(String name, String importFile) {
        this.name = name;
        this.importFile = importFile;
        isScalarType = false;
    }

    public String getName() {
        return name;
    }

    public String getImportFile() {
        return importFile;
    }

    public boolean isScalarType() {
        return isScalarType;
    }
}
