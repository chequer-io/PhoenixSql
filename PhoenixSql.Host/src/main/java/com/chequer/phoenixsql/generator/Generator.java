package com.chequer.phoenixsql.generator;

import com.chequer.phoenixsql.generator.proto.*;
import com.google.common.reflect.ClassPath;
import org.apache.phoenix.parse.*;
import org.reflections.Reflections;
import org.reflections.scanners.MethodParameterScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.io.File;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.concurrent.Executors;

@SuppressWarnings("UnstableApiUsage")
public class Generator {
    private static String javaProjectDir;
    private static String csharpProjectDir;
    private static String protoDir;

    private static ProtoFile protoFile;
    private static Map<Class<?>, List<ProtoField>> abstractsFields;
    private static Map<Class<?>, ProtoMessage> messages;

    private static final List<String> excludeProperties = new ArrayList<>() {{
        add("org.apache.phoenix.parse.BinaryParseNode.getLHS");
        add("org.apache.phoenix.parse.BinaryParseNode.getRHS");
        add("*.isStateless");
    }};

    private static final Map<Class<?>, ProtoType> protoScalarTypes = new HashMap<>() {{
        put(double.class, new ProtoType(ProtoScalaType.DOUBLE));
        put(Double.class, new ProtoType(ProtoScalaType.DOUBLE));
        put(float.class, new ProtoType(ProtoScalaType.FLOAT));
        put(Float.class, new ProtoType(ProtoScalaType.FLOAT));
        put(int.class, new ProtoType(ProtoScalaType.INT32));
        put(Integer.class, new ProtoType(ProtoScalaType.INT32));
        put(long.class, new ProtoType(ProtoScalaType.INT64));
        put(Long.class, new ProtoType(ProtoScalaType.INT64));
        put(boolean.class, new ProtoType(ProtoScalaType.BOOL));
        put(Boolean.class, new ProtoType(ProtoScalaType.BOOL));
        put(String.class, new ProtoType(ProtoScalaType.STRING));
        put(byte[].class, new ProtoType(ProtoScalaType.BYTES));
    }};

    public static void main(String[] args) throws Exception {
        javaProjectDir = System.getProperty("user.dir");
        csharpProjectDir = new File(javaProjectDir, "../PhoenixSql").getCanonicalPath();
        protoDir = new File(javaProjectDir, "../proto").getCanonicalPath();

        protoFile = new ProtoFile();
        protoFile.setSyntax("proto3");
        protoFile.setPackageName("proto");
        protoFile.options().put("csharp_namespace", "PhoenixSql");
        protoFile.options().put("java_package", "com.chequer.phoenixsql.proto");

        abstractsFields = new HashMap<>();
        messages = new HashMap<>();

        generate();

        var writer = new StringWriter();
        var protoWriter = new ProtoWriter(writer);
        protoWriter.write(protoFile);

        System.out.println("--------------------------------------------------------------------");
        System.out.println(writer);
    }

    public static void generate() throws Exception {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();

        var r = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("org.apache.phoenix.parse"))
                .setScanners(new SubTypesScanner())
                .setExecutorService(Executors.newFixedThreadPool(4)));

        var d = r.getSubTypesOf(ParseNode.class);
        ClassPath.from(loader).getTopLevelClasses()
                .stream()
                .filter(i -> i.getName().startsWith("org.apache.phoenix.parse."))
                .map(ClassPath.ClassInfo::load)
                .sorted((o1, o2) -> {
                    var modifier1 = Modifier.isAbstract(o1.getModifiers());
                    var modifier2 = Modifier.isAbstract(o2.getModifiers());

                    if (modifier1 != modifier2) {
                        return modifier1 ? -1 : 1;
                    }

                    int level1 = getInheritanceLevel(o1);
                    int level2 = getInheritanceLevel(o2);

                    if (level1 != level2) {
                        return level1 < level2 ? -1 : 1;
                    }

                    return 0;
                })
                .forEach(clazz -> {
                    final String name = clazz.getName();

                    if (ParseNode.class.isAssignableFrom(clazz)) {
                        generateParseNode(clazz);
                    } else if (name.endsWith("Node")) {
                        generateNode(clazz);
                    }
                });
    }

    private static void generateNode(Class<?> clazz) {
        if (messages.containsKey(clazz)) {
            return;
        }

        var isAbstract = Modifier.isAbstract(clazz.getModifiers());
        var messageName = getProtoMessageName(clazz);

        var message = new ProtoMessage(messageName);
        message.setComment(new ProtoSingLineComment(clazz.getCanonicalName()));

        protoFile.add(message);
        messages.put(clazz, message);

//         if (clazz.getSuperclass() != Object.class) {
//            // TODO: non abstract class
//        }

        var fields = createProtoFields(clazz);

        if (isAbstract) {
            System.out.printf("%s (abstract)%n", clazz.getSimpleName());

            abstractsFields.put(clazz, fields);

            var oneOfField = new ProtoFieldOneOf();
            oneOfField.setName("inherit");

            message.add(oneOfField);
        } else {
            System.out.println(clazz.getSimpleName());

            var classMap = getClassMap(clazz);

            for (int i = 0; i < classMap.length - 1; i++) {
                if (abstractsFields.containsKey(classMap[i])) {
                    var abstractMessage = messages.getOrDefault(classMap[i], null);

                    if (abstractMessage != null) {
                        var fieldOneOf = (ProtoFieldOneOf) abstractMessage.get(0);
                        var field = new ProtoField();

                        fieldOneOf.fields().add(field);

                        field.setType(new ProtoType(messageName, null));
                        field.setName("_" + fieldOneOf.fields().size());
                    }
                }

                var absFields = abstractsFields.getOrDefault(classMap[i], null);

                if (absFields != null) {
                    for (final var absField : absFields) {
                        message.add(absField);
                    }
                }
            }

            for (final var field : fields) {
                message.add(field);
            }
        }
    }

    private static void generateParseNode(Class<?> clazz) {
        generateNode(clazz);

        // TODO: implement
    }

    private static List<ProtoField> createProtoFields(Class<?> clazz) {
        var fields = new ArrayList<ProtoField>();

        for (final var method : getProperties(clazz)) {
            var fieldName = method.getName();

            // getName -> name
            if (fieldName.startsWith("get")) {
                fieldName = convertToCamelCase(fieldName.substring(3));
            }

            var protoField = new ProtoField();
            protoField.setName(fieldName);

            var returnType = method.getReturnType();

            if (List.class.isAssignableFrom(returnType)) {
                var parameterizedType = (ParameterizedType) method.getGenericReturnType();
                returnType = (Class<?>) parameterizedType.getActualTypeArguments()[0];

                protoField.setRepeated(true);
            }

//            if (returnType.isArray() || Iterable.class.isAssignableFrom(returnType)) {
//                throw new Exception("not supported type");
//            }

            var protoType = protoScalarTypes.getOrDefault(returnType, null);

            if (protoType == null) {
                if (returnType.isEnum()) {
                    // TODO:
                    continue;
                } else {
                    protoType = new ProtoType(getProtoMessageName(returnType), null);
                }
            }

            protoField.setType(protoType);

            fields.add(protoField);
        }

        return fields;
    }

    private static String getProtoMessageName(Class<?> clazz) {
        var messageName = clazz.getSimpleName();

        if (Modifier.isAbstract(clazz.getModifiers())) {
            messageName += "Abs";
        }

        return messageName;
    }

    private static int getInheritanceLevel(Class<?> clazz) {
        int level = 0;

        while (clazz != null && clazz != Object.class) {
            level++;
            clazz = clazz.getSuperclass();
        }

        return level;
    }

    private static Class<?>[] getClassMap(Class<?> clazz) {
        var queue = new LinkedList<Class<?>>();

        while (clazz != null && clazz != Object.class) {
            queue.push(clazz);
            clazz = clazz.getSuperclass();
        }

        var buffer = new Class<?>[queue.size()];
        queue.toArray(buffer);

        return buffer;
    }

    private static Method[] getProperties(Class<?> clazz) {
        var list = new ArrayList<Method>();

        for (final var method : clazz.getDeclaredMethods()) {
            if (Modifier.isStatic(method.getModifiers())) {
                continue;
            }

            if (method.getParameterCount() > 0) {
                continue;
            }

            var type = method.getReturnType();
            var name = method.getName();

            if (type == Void.class) {
                continue;
            }

            if (!name.startsWith("get") && !name.startsWith("is")) {
                continue;
            }

            var path = String.format("%s.%s", clazz.getCanonicalName(), method.getName());

            if (excludeProperties.stream().anyMatch(p -> isMatch(path, p))) {
                continue;
            }

            if (isOverrideMethod(method)) {
                continue;
            }

            list.add(method);
        }

        var buffer = new Method[list.size()];
        list.toArray(buffer);

        return buffer;
    }

    private static boolean isOverrideMethod(Method method) {
        var superClazz = method.getDeclaringClass().getSuperclass();

        try {
            var superMethod = superClazz.getMethod(method.getName());
            return superMethod.getReturnType() == method.getReturnType();
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    private static String convertToCamelCase(String value) {
        return Character.toLowerCase(value.charAt(0)) + value.substring(1);
    }

    private static boolean isMatch(String s, String p) {
        int i = 0;
        int j = 0;
        int starIndex = -1;
        int iIndex = -1;

        while (i < s.length()) {
            if (j < p.length() && (p.charAt(j) == '?' || p.charAt(j) == s.charAt(i))) {
                ++i;
                ++j;
            } else if (j < p.length() && p.charAt(j) == '*') {
                starIndex = j;
                iIndex = i;
                j++;
            } else if (starIndex != -1) {
                j = starIndex + 1;
                i = iIndex + 1;
                iIndex++;
            } else {
                return false;
            }
        }

        while (j < p.length() && p.charAt(j) == '*') {
            ++j;
        }

        return j == p.length();
    }
}
