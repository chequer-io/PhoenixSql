package com.chequer.phoenixsql.generator.proto;

public abstract class ProtoComment extends ProtoMember {
    private final String content;

    public ProtoComment(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
