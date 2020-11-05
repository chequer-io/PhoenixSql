package com.chequer.phoenixsql.generator.proto;

public class ProtoField extends ProtoMember {
    private boolean isRepeated;
    private ProtoType type;
    private String name;

    public ProtoField() {
    }

    public ProtoField(String name, ProtoType type) {
        this.name = name;
        this.type = type;
    }

    public ProtoField(String name, ProtoScalaType type) {
        this.name = name;
        this.type = new ProtoType(type);
    }

    public ProtoField(String name, String type) {
        this(name, new ProtoType(type, null));
    }

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
