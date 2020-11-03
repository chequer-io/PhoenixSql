package com.chequer.phoenixsql.generator.proto;

import java.util.ArrayList;
import java.util.List;

public class ProtoEnum extends ProtoMember {
    private final String name;
    private final List<String> values = new ArrayList<>();

    public ProtoEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<String> values() {
        return values;
    }
}
