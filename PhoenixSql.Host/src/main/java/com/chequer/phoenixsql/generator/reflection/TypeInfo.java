package com.chequer.phoenixsql.generator.reflection;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class TypeInfo {
    private final static Map<Class<?>, TypeInfo> typeInfos = new HashMap<>();
    private final static Map<ParameterizedType, TypeInfo> parameterizedTypeInfos = new HashMap<>();

    private final Class<?> clazz;
    private final ParameterizedType parameterizedType;
    private final TypeInfo baseType;
    private final int modifier;

    private List<MethodInfo> methods;
    private List<MethodInfo> declaredMethods;
    private List<TypeInfo> genericTypeArguments;

    public static TypeInfo get(Type type) {
        if (type instanceof ParameterizedType) {
            return get((ParameterizedType) type);
        }

        return get((Class<?>) type);
    }

    public static TypeInfo get(Class<?> clazz) {
        var typeInfo = typeInfos.getOrDefault(clazz, null);

        if (typeInfo == null) {
            typeInfo = new TypeInfo(clazz);
            typeInfos.put(clazz, typeInfo);
        }

        return typeInfo;
    }

    public static TypeInfo get(ParameterizedType type) {
        var typeInfo = parameterizedTypeInfos.getOrDefault(type, null);

        if (typeInfo == null) {
            typeInfo = new TypeInfo(type);
            parameterizedTypeInfos.put(type, typeInfo);
        }

        return typeInfo;
    }

    private TypeInfo(Class<?> clazz) {
        this.clazz = clazz;
        parameterizedType = null;
        baseType = getBaseType(clazz);
        modifier = clazz.getModifiers();
    }

    private TypeInfo(ParameterizedType type) {
        parameterizedType = type;
        clazz = (Class<?>) type.getRawType();
        baseType = getBaseType(clazz);
        modifier = clazz.getModifiers();
    }

    private static TypeInfo getBaseType(Class<?> clazz) {
        TypeInfo baseType = null;

        if (clazz.getSuperclass() != null) {
            baseType = TypeInfo.get(clazz.getSuperclass());
        }

        if (baseType == null || baseType.clazz == Object.class && !clazz.isInterface()) {
            var interfaces = clazz.getInterfaces();

            if (interfaces.length == 1) {
                baseType = TypeInfo.get(interfaces[0]);
            } else if (interfaces.length > 1) {
                System.out.println(clazz);
            }
        }

        return baseType;
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
            methods = Arrays.stream(clazz.getMethods())
                    .sorted(Comparator.comparing(Method::getName))
                    .map(MethodInfo::new)
                    .collect(Collectors.toList());
        }

        return Collections.unmodifiableList(methods);
    }

    public List<MethodInfo> getDeclaredMethods() {
        if (declaredMethods == null) {
            declaredMethods = new ArrayList<>();

            for (final var method : getMethods()) {
                if (method.getDeclaringType().equals(this)) {
                    declaredMethods.add(method);
                }
            }
        }

        return Collections.unmodifiableList(declaredMethods);
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

    public List<TypeInfo> getGenericTypeArguments() {
        if (genericTypeArguments == null) {
            genericTypeArguments = new ArrayList<>();

            if (parameterizedType != null) {
                for (final var type : parameterizedType.getActualTypeArguments()) {
                    genericTypeArguments.add(TypeInfo.get(type));
                }
            }
        }

        return Collections.unmodifiableList(genericTypeArguments);
    }

    public TypeInfo getElementType() {
        return TypeInfo.get(clazz.getComponentType());
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

    public boolean isArray() {
        return clazz.isArray();
    }

    public boolean isEnum() {
        return clazz.isEnum();
    }

    public boolean isAnonymousClass() {
        return clazz.isAnonymousClass();
    }

    public boolean isLocalClass() {
        return clazz.isLocalClass();
    }

    public boolean isMemberClass() {
        return clazz.isMemberClass();
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
