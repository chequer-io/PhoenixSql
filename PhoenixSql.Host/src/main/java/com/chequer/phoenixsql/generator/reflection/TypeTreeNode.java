package com.chequer.phoenixsql.generator.reflection;

import java.util.ArrayList;
import java.util.List;

public class TypeTreeNode {
    private TypeTreeNode parent;
    public final TypeInfo typeInfo;
    public final List<TypeTreeNode> children = new ArrayList<>();

    public TypeTreeNode(TypeInfo typeInfo) {
        this.typeInfo = typeInfo;
    }

    public boolean hasChildren() {
        return children.size() > 0;
    }

    public TypeTreeNode getParent() {
        return parent;
    }

    public void setParent(TypeTreeNode parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return typeInfo.toString();
    }
}
