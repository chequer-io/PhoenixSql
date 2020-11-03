package com.chequer.phoenixsql.generator.proto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class ProtoContainer extends ProtoMember implements Iterable<ProtoMember> {
    private final List<ProtoMember> members;

    public ProtoContainer() {
        members = new ArrayList<>();
    }

    public int size() {
        return members.size();
    }

    public ProtoMember get(int index) {
        return members.get(index);
    }

    public void add(ProtoMember member) {
        members.add(member);
    }

    public void add(int index, ProtoMember member) {
        members.add(member);
    }

    public void remove(ProtoMember member) {
        members.remove(member);
    }

    public void remove(int index) {
        members.remove(index);
    }

    public void clear() {
        members.clear();
    }

    @Override
    public Iterator<ProtoMember> iterator() {
        return members.iterator();
    }
}
