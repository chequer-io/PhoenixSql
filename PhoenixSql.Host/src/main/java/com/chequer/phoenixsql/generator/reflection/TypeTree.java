package com.chequer.phoenixsql.generator.reflection;

import java.util.*;
import java.util.function.Predicate;

public class TypeTree {
    public final TypeTreeNode root;

    private TypeTree(TypeTreeNode root) {
        this.root = root;
    }

    public static TypeTree build(List<TypeInfo> typeInfos, Predicate<TypeInfo> buildNodePredicate) {
        return new TypeTreeBuilder(typeInfos, buildNodePredicate).build();
    }

    private static class TypeTreeBuilder {
        private final Map<TypeInfo, TypeTreeNode> typeMap = new HashMap<>();
        private final Predicate<TypeInfo> buildNodePredicate;
        private final Set<TypeInfo> typeInfoSet;

        public TypeTreeBuilder(List<TypeInfo> typeInfos, Predicate<TypeInfo> buildNodePredicate) {
            this.buildNodePredicate = buildNodePredicate;
            typeInfoSet = new HashSet<>(typeInfos);
        }

        public TypeTree build() {
            for (final var typeInfo : typeInfoSet) {
                buildNode(typeInfo);
            }

            var root = new TypeTreeNode(null);

            for (final var typeNode : typeMap.values()) {
                if (typeNode.getParent() == null) {
                    root.children.add(typeNode);
                }
            }

            return new TypeTree(root);
        }

        private TypeTreeNode buildNode(TypeInfo typeInfo) {
            if (!typeInfoSet.contains(typeInfo)) {
                return null;
            }

            if (!buildNodePredicate.test(typeInfo)) {
                return null;
            }

            var typeNode = typeMap.getOrDefault(typeInfo, null);

            if (typeNode == null) {
                typeNode = new TypeTreeNode(typeInfo);
                typeMap.put(typeInfo, typeNode);

                if (typeInfo.getBaseType() != null) {
                    var parentTypeNode = buildNode(typeInfo.getBaseType());

                    if (parentTypeNode != null) {
                        typeNode.setParent(parentTypeNode);
                        parentTypeNode.children.add(typeNode);
                    }
                }
            }

            return typeNode;
        }
    }
}
