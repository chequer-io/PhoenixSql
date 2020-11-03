package com.chequer.phoenixsql.generator.proto;

public class ProtoMessage extends ProtoContainer {
    private String name;

    public ProtoMessage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
