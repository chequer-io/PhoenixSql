package com.chequer.phoenixsql.generator.reflection;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

@SuppressWarnings("UnstableApiUsage")
public class Reflections {
    public static ImmutableSet<ClassPath.ClassInfo> getTopLevelClasses(String packageName) throws IOException {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        return ClassPath.from(loader).getTopLevelClasses(packageName);
    }

    public static boolean isAbstract(Class<?> clazz) {
        return Modifier.isAbstract(clazz.getModifiers());
    }

    public static boolean isStatic(Class<?> clazz) {
        return Modifier.isStatic(clazz.getModifiers());
    }

    public static boolean isOverrideMethod(Method method) {
        var superClass = method.getDeclaringClass().getSuperclass();

        try {
            var superMethod = superClass.getDeclaredMethod(method.getName(), method.getParameterTypes());
            return superMethod.getReturnType() == method.getReturnType();
        } catch (NoSuchMethodException e) {
            return false;
        }
    }
}
