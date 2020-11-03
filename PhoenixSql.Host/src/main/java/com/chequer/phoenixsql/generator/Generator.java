package com.chequer.phoenixsql.generator;

import com.chequer.phoenixsql.generator.proto.*;
import com.google.common.reflect.ClassPath;
import org.apache.phoenix.parse.*;

import java.io.File;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

@SuppressWarnings("UnstableApiUsage")
public class Generator {
    private static String javaProjectDir;
    private static String csharpProjectDir;
    private static String protoDir;

    private static ProtoFile protoFile;

    public static void main(String[] args) throws Exception {
        javaProjectDir = System.getProperty("user.dir");
        csharpProjectDir = new File(javaProjectDir, "../PhoenixSql").getCanonicalPath();
        protoDir = new File(javaProjectDir, "../proto").getCanonicalPath();

        protoFile = new ProtoFile();
        protoFile.setSyntax("proto3");
        protoFile.setPackageName("proto");
        protoFile.options().put("csharp_namespace", "PhoenixSql");
        protoFile.options().put("java_package", "com.chequer.phoenixsql.proto");

        generate();
    }

    public static void generate() throws Exception {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();

        for (final ClassPath.ClassInfo info : ClassPath.from(loader).getTopLevelClasses()) {
            if (!info.getName().startsWith("org.apache.phoenix.parse.")) {
                continue;
            }

            final Class<?> clazz = info.load();
            final String name = clazz.getName();

            if (ParseNode.class.isAssignableFrom(clazz)) {
                generateParseNode(clazz);
            } else if (name.endsWith("Node")) {
                generateNode(clazz);
            }
        }
    }

    private static void generateNode(Class<?> clazz) {
        if (Modifier.isAbstract(clazz.getModifiers())) {
            System.out.printf("%s (abstract)%n", clazz.getSimpleName());
        } else {
            System.out.println(clazz.getSimpleName());
        }

        for (final var field : clazz.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            System.out.printf(" - %s\n", field.getName());
        }
    }

    private static void generateParseNode(Class<?> clazz) {
        // TODO: implement
    }

    private static Class<?>[] getClassMap(Class<?> clazz) {
        var queue = new LinkedList<Class<?>>();

        while (clazz != Object.class) {
            queue.push(clazz);
            clazz = clazz.getSuperclass();
        }

        var buffer = new Class<?>[queue.size()];
        queue.toArray(buffer);

        return buffer;
    }

    private static Field[] getLocalFields(Class<?> clazz) {
        var list = new ArrayList<Field>();

        for (final var field : clazz.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            list.add(field);
        }

        var buffer = new Field[list.size()];
        list.toArray(buffer);

        return buffer;
    }
}
