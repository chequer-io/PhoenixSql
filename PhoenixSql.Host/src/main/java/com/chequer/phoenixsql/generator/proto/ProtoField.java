package com.chequer.phoenixsql.generator.proto;

public class ProtoField extends ProtoMember {
    private boolean isRepeated;
    private ProtoType type;
    private String name;

    public boolean isRepeated() {
        return isRepeated;
    }

    public ProtoField setRepeated(boolean repeated) {
        isRepeated = repeated;
        return this;
    }

    public ProtoType getType() {
        return type;
    }

    public ProtoField setType(ProtoType type) {
        this.type = type;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProtoField setName(String name) {
        this.name = name;
        return this;
    }
}
