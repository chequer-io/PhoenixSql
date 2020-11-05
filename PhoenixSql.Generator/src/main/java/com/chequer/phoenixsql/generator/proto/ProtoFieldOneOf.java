package com.chequer.phoenixsql.generator.proto;

import java.util.ArrayList;
import java.util.List;

public class ProtoFieldOneOf extends ProtoField {
    private final List<ProtoField> fields = new ArrayList<>();

    public List<ProtoField> fields() {
        return fields;
    }
}
