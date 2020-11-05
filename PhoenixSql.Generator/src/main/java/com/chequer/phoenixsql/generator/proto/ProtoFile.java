package com.chequer.phoenixsql.generator.proto;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ProtoFile extends ProtoContainer {
    private String syntax;
    private String packageName;
    private final Map<String, String> options = new HashMap<>();
    private final HashSet<String> imports = new HashSet<>();

    public String getSyntax() {
        return syntax;
    }

    public void setSyntax(String syntax) {
        this.syntax = syntax;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Map<String, String> options() {
        return options;
    }

    public HashSet<String> imports() {
        return imports;
    }
}
