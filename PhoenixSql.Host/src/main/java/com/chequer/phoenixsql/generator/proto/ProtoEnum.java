package com.chequer.phoenixsql.generator.proto;

import java.util.ArrayList;
import java.util.List;

public class ProtoEnum extends ProtoMember {
    private String name;
    private final List<String> values = new ArrayList<>();

    public String getName() {
        return name;
    }

    public List<String> values() {
        return values;
    }
}
