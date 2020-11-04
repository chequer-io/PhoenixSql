package com.chequer.phoenixsql.generator.reflection;

import com.google.common.base.Objects;

import java.lang.reflect.Modifier;
import java.util.*;

public class TypeInfo {
    private final static Map<Class<?>, TypeInfo> typeInfos = new HashMap<>();

    private final Class<?> clazz;
    private final TypeInfo baseType;
    private final int modifier;

    private List<MethodInfo> methods;

    public static TypeInfo get(Class<?> clazz) {
        var typeInfo = typeInfos.getOrDefault(clazz, null);

        if (typeInfo == null) {
            typeInfo = new TypeInfo(clazz);
            typeInfos.put(clazz, typeInfo);
        }

        return typeInfo;
    }

    private TypeInfo(Class<?> clazz) {
        this.clazz = clazz;

        if (clazz.getSuperclass() != null) {
            baseType = TypeInfo.get(clazz.getSuperclass());
        } else {
            baseType = null;
        }

        modifier = clazz.getModifiers();
    }

    public Class<?> unwrap() {
        return clazz;
    }

    public TypeInfo getBaseType() {
        return baseType;
    }

    public String getName() {
        return clazz.getSimpleName();
    }

    public String getPackageName() {
        return clazz.getPackageName();
    }

    public String getFullName() {
        return clazz.getName();
    }

    public List<MethodInfo> getMethods() {
        if (methods == null) {
            methods = new ArrayList<>();

            for (final var method : clazz.getMethods()) {
                methods.add(new MethodInfo(method));
            }
        }

        return Collections.unmodifiableList(methods);
    }

    public MethodInfo getMethod(String name) {
        for (final var method : getMethods()) {
            if (method.getName().equals(name)) {
                return method;
            }
        }

        return null;
    }

    public int getModifier() {
        return modifier;
    }

    public boolean isInstanceOf(Object instance) {
        return isAssignableFrom(instance.getClass());
    }

    public boolean isAssignableFrom(TypeInfo typeInfo) {
        return isAssignableFrom(typeInfo.clazz);
    }

    public boolean isAssignableFrom(Class<?> clazz) {
        return this.clazz.isAssignableFrom(clazz);
    }

    public boolean isNative() {
        return Modifier.isNative(modifier);
    }

    public boolean isAbstract() {
        return Modifier.isAbstract(modifier);
    }

    public boolean isPublic() {
        return Modifier.isPublic(modifier);
    }

    public boolean isPrivate() {
        return Modifier.isPrivate(modifier);
    }

    public boolean isInterface() {
        return Modifier.isInterface(modifier);
    }

    public boolean isProtected() {
        return Modifier.isProtected(modifier);
    }

    public boolean isStatic() {
        return Modifier.isStatic(modifier);
    }

    @Override
    public String toString() {
        return clazz.getName();
    }

    @Override
    public boolean equals(Object o) {
        var info = (TypeInfo) o;
        return info.clazz.equals(clazz);
    }

    @Override
    public int hashCode() {
        return clazz.hashCode();
    }
}
