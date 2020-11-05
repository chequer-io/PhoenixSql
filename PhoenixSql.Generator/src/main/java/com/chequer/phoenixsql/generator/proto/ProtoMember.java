package com.chequer.phoenixsql.generator.proto;

public abstract class ProtoMember {
    private ProtoComment comment;

    public ProtoComment getComment() {
        return comment;
    }

    public void setComment(ProtoComment comment) {
        this.comment = comment;
    }
}
