package com.chequer.phoenixsql.generator.reflection;

import java.lang.reflect.*;

public class MethodInfo {
    private final Method method;
    private final TypeInfo declaringType;
    private final int modifier;
    private final MethodInfo baseDefinition;

    public MethodInfo(Method method) {
        this.method = method;
        modifier = method.getModifiers();
        declaringType = TypeInfo.get(method.getDeclaringClass());

        if (declaringType.getBaseType() != null) {
            baseDefinition = declaringType.getBaseType().getMethod(method.getName());
        } else {
            baseDefinition = null;
        }
    }

    public String getName() {
        return method.getName();
    }

    public TypeInfo getDeclaringType() {
        return declaringType;
    }

    public MethodInfo getBaseDefinition() {
        return baseDefinition;
    }

    public Parameter[] getParameters() {
        return method.getParameters();
    }

    public int getParameterCount() {
        return method.getParameterCount();
    }

    public TypeInfo getReturnType() {
        return TypeInfo.get(method.getReturnType());
    }

    public Type getGenericReturnType() {
        return method.getGenericReturnType();
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
        var parameters = new StringBuilder();

        for (final var parameter : method.getParameters()) {
            if (parameters.length() > 0) {
                parameters.append(", ");
            }

            parameters.append(parameter.getParameterizedType().getTypeName());
        }

        return String.format("%s %s(%s)", method.getReturnType().getName(), method.getName(), parameters);
    }

    @Override
    public boolean equals(Object o) {
        var info = (MethodInfo) o;
        return method.equals(info.method);
    }

    @Override
    public int hashCode() {
        return method.hashCode();
    }
}
