package com.chequer.phoenixsql.generator;

import com.chequer.phoenixsql.generator.proto.*;
import com.chequer.phoenixsql.generator.reflection.*;
import org.apache.phoenix.expression.LiteralExpression;
import org.apache.phoenix.parse.*;
import org.apache.phoenix.schema.PTableKey;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("UnstableApiUsage")
public class Generator {
    private static String javaProjectDir;
    private static File csharpNodeDir;
    private static File protoFile;

    private static ProtoFile proto;
    private static TypeTree typeTree;

    private static final ProtoType udfMapEntryProtoType = new ProtoType("UDFMapEntry", null);

    private static final TypeInfo listTypeInfo = TypeInfo.get(List.class);
    private static final TypeInfo mapTypeInfo = TypeInfo.get(Map.class);
    private static final TypeInfo hbasePairType = TypeInfo.get(org.apache.hadoop.hbase.util.Pair.class);
    private static final TypeInfo literalExpression = TypeInfo.get(LiteralExpression.class);

    private static final List<TypeInfo> additionalGenerateTypes = new ArrayList<>() {{
        add(TypeInfo.get(PrimaryKeyConstraint.class));
        add(TypeInfo.get(TableName.class));
        add(TypeInfo.get(ColumnName.class));
        add(TypeInfo.get(ColumnDef.class));
        add(TypeInfo.get(IndexKeyConstraint.class));
        add(TypeInfo.get(PFunction.class));
        add(TypeInfo.get(PFunction.FunctionArgument.class));
        add(TypeInfo.get(PTableKey.class));
        add(TypeInfo.get(CursorName.class));
    }};

    private static final Set<String> excludeProperties = new HashSet<>() {{
        add("org.apache.phoenix.parse.BinaryParseNode.getLHS");
        add("org.apache.phoenix.parse.BinaryParseNode.getRHS");
        add("org.apache.phoenix.parse.FunctionParseNode.getInfo");
        add("org.apache.phoenix.parse.LiteralParseNode.getBytes");
        add("org.apache.phoenix.parse.HintNode.isEmpty");
        add("org.apache.phoenix.parse.ColumnName.getColumnName");
        add("org.apache.phoenix.parse.ColumnName.getFamilyName");

        add("org.apache.phoenix.parse.AddColumnStatement.getProps");
        add("org.apache.phoenix.parse.AlterIndexStatement.getProps");
        add("org.apache.phoenix.parse.CreateTableStatement.getProps");
        add("org.apache.phoenix.parse.CreateIndexStatement.getProps");
        add("org.apache.phoenix.parse.AlterSessionStatement.getProps");
        add("org.apache.phoenix.parse.UpdateStatisticsStatement.getProps");
    }};

    private static final Map<TypeInfo, ProtoType> protoScalarTypes = new HashMap<>() {{
        put(TypeInfo.get(double.class), new ProtoType(ProtoScalaType.DOUBLE));
        put(TypeInfo.get(Double.class), new ProtoType(ProtoScalaType.DOUBLE));
        put(TypeInfo.get(float.class), new ProtoType(ProtoScalaType.FLOAT));
        put(TypeInfo.get(Float.class), new ProtoType(ProtoScalaType.FLOAT));
        put(TypeInfo.get(short.class), new ProtoType(ProtoScalaType.INT32));
        put(TypeInfo.get(Short.class), new ProtoType(ProtoScalaType.INT32));
        put(TypeInfo.get(int.class), new ProtoType(ProtoScalaType.INT32));
        put(TypeInfo.get(Integer.class), new ProtoType(ProtoScalaType.INT32));
        put(TypeInfo.get(long.class), new ProtoType(ProtoScalaType.INT64));
        put(TypeInfo.get(Long.class), new ProtoType(ProtoScalaType.INT64));
        put(TypeInfo.get(boolean.class), new ProtoType(ProtoScalaType.BOOL));
        put(TypeInfo.get(Boolean.class), new ProtoType(ProtoScalaType.BOOL));
        put(TypeInfo.get(String.class), new ProtoType(ProtoScalaType.STRING));
        put(TypeInfo.get(Object.class), new ProtoType("google.protobuf.Any", "google/protobuf/any.proto"));
    }};

    private static final Map<String, String> csScalarTypes = new HashMap<>() {{
        put(ProtoScalaType.DOUBLE.value, "double");
        put(ProtoScalaType.FLOAT.value, "float");
        put(ProtoScalaType.INT32.value, "int");
        put(ProtoScalaType.INT64.value, "long");
        put(ProtoScalaType.BOOL.value, "bool");
        put(ProtoScalaType.STRING.value, "string");
    }};

    private static final Map<TypeInfo, GenerateData> generateData = new HashMap<>();

    public static void main(String[] args) throws Exception {
        javaProjectDir = System.getProperty("user.dir");
        csharpNodeDir = new File(javaProjectDir, "../PhoenixSql/Proto/Nodes");
        protoFile = new File(javaProjectDir, "../proto/nodes.proto");

        proto = new ProtoFile();
        proto.setSyntax("proto3");
        proto.setPackageName("proto");
        proto.options().put("csharp_namespace", "PhoenixSql");
        proto.options().put("java_package", "com.chequer.phoenixsql.proto");

        proto.add(createPDataType());
        proto.add(createPName());
        proto.add(createUDFMapEntry());

        generate();

        if (protoFile.exists()) {
            protoFile.delete();
        }

        var writer = new FileWriter(protoFile);
        var protoWriter = new ProtoWriter(writer);
        protoWriter.write(proto);
        writer.close();

        writeCSharpInterfaces();
    }

    private static ProtoEnum createPDataType() {
        var protoEnum = new ProtoEnum("PDataType");
        var values = protoEnum.values();
        values.add("PUnsignedDate");
        values.add("PArrayDataType");
        values.add("PBinaryBase");
        values.add("PBoolean");
        values.add("PChar");
        values.add("PDate");
        values.add("PNumericType");
        values.add("PTime");
        values.add("PTimestamp");
        values.add("PUnsignedTime");
        values.add("PVarchar");

        return protoEnum;
    }

    private static ProtoMessage createPName() {
        var message = new ProtoMessage("PName");
        message.add(new ProtoField("value", ProtoScalaType.STRING));
        return message;
    }

    // Map<String, UDFParseNode>
    private static ProtoMessage createUDFMapEntry() {
        var message = new ProtoMessage("UDFMapEntry");
        message.add(new ProtoField("key", ProtoScalaType.STRING));
        message.add(new ProtoField("value", "UDFParseNode"));
        return message;
    }

    private static void generate() throws Exception {
        var generateTypes = Reflections.getTopLevelClasses("org.apache.phoenix.parse")
                .stream()
                .filter(i -> i.getName().endsWith("Node") || i.getName().endsWith("Statement"))
                .map(i -> TypeInfo.get(i.load()));

        var types = Stream.concat(additionalGenerateTypes.stream(), generateTypes)
                .filter(t -> !t.isNative())
                .collect(Collectors.toList());

        typeTree = TypeTree.build(types, Generator::isPhoenixType);

        generate(typeTree.root);
    }

    private static void writeCSharpInterfaces() throws IOException {
        System.out.println("---------");

//        if (csharpNodeDir.exists()) {
//            csharpNodeDir.delete();
//        }

        csharpNodeDir.mkdir();

        for (final var data : generateData.values()) {
            if (data.csTypeName == null) {
                continue;
            }

            var csFile = Paths.get(csharpNodeDir.getCanonicalPath(), data.csTypeName + ".cs").toAbsolutePath().toString();
            var csWriter = new FileWriter(csFile);

            var baseData = generateData.getOrDefault(data.type.getBaseType(), null);

            csWriter.write("using System.Collections.Generic;\n\n");
            csWriter.write("namespace PhoenixSql\n");
            csWriter.write("{\n");

            if (data.csInterface) {
                csWriter.write(String.format("    public interface %s", data.csTypeName));

                if (baseData != null) {
                    csWriter.write(" : ");
                    csWriter.write(baseData.csTypeName);
                }

                csWriter.write('\n');
                csWriter.write("    {\n");

                for (int i = 0; i < data.csProperties.size(); i++) {
                    if (i > 0) {
                        csWriter.write("\n");
                    }

                    var property = data.csProperties.get(i);
                    csWriter.write(String.format("        %s %s { get; }\n", property.type, property.name));
                }

                csWriter.write("    }\n");
            } else {
                csWriter.write(String.format("    public partial class %s", data.csTypeName));

                if (baseData != null) {
                    csWriter.write(" : ");
                    csWriter.write(baseData.csTypeName);
                }

                csWriter.write('\n');
                csWriter.write("    {\n");

                if (ParseNode.class.isAssignableFrom(data.type.unwrap())) {
                    csWriter.write("        IReadOnlyList<IParseNode> IParseNode.Children => children_;\n");
                }

                csWriter.write("    }\n");
            }

            csWriter.write("}\n");

            csWriter.close();
        }
    }

    private static void generate(TypeTreeNode node) {
        if (node.typeInfo != null) {
            if (generateData.containsKey(node.typeInfo)) {
                return;
            }

            var data = new GenerateData(node.typeInfo);
            data.message = new ProtoMessage(getProtoMessageName(node));

            generateData.put(node.typeInfo, data);

            List<GenerateData> inheritMap = new ArrayList<>();

            if (!node.hasChildren()) {
                var parentNode = node.getParent();

                while (parentNode != null) {
                    inheritMap.add(0, generateData.get(parentNode.typeInfo));
                    parentNode = parentNode.getParent();
                }

                for (final var inherit : inheritMap) {
                    for (final var inheritField : inherit.inheritMembers) {
                        data.message.add(inheritField);
                    }
                }
            }

            var protoMembers = new ArrayList<ProtoMember>();
            var csProperties = new ArrayList<CSharpPropertyInfo>();

            protoMembers.add(new ProtoSingLineComment(node.typeInfo.getFullName()));

            System.out.println(node.typeInfo.getName());

            for (final var property : getProperties(node.typeInfo)) {
                var protoField = new ProtoField();
                var returnType = property.getReturnType();

                if (returnType.isArray()) {
                    returnType = returnType.getElementType();
                    protoField.setRepeated(true);
                } else if (listTypeInfo.isAssignableFrom(returnType)) {
                    var genericReturnType = TypeInfo.get((ParameterizedType) property.getGenericReturnType());
                    returnType = genericReturnType.getGenericTypeArguments().get(0);
                    protoField.setRepeated(true);
                }

                ProtoType protoType = null;

                if (mapTypeInfo.isAssignableFrom(returnType) && property.getName().equals("getUdfParseNodes")) {
                    protoField.setRepeated(true);
                    protoType = udfMapEntryProtoType;
                }

                if (protoType == null) {
                    protoType = resolveProtoType(data.message, returnType);
                }

                protoField.setType(protoType);
                protoField.setName(getProtoFieldName(property));

                protoMembers.add(protoField);
                csProperties.add(new CSharpPropertyInfo(
                        getCSharpTypeName(returnType, protoType, protoField.isRepeated()),
                        getCSharpPropertyName(property)));

                System.out.printf(" * %s %s%n", protoType.getName(), protoField.getName());
            }

            if (node.hasChildren()) {
                data.inheritMembers = protoMembers;

                data.csInterface = true;
                data.csTypeName = getCSharpTypeName(node.typeInfo, null, false);
                data.csProperties = csProperties;

                data.inheritField = new ProtoFieldOneOf();
                data.inheritField.setName("inherit");

                data.message.add(data.inheritField);
            } else {
                data.csTypeName = data.message.getName();

                for (final var protoField : protoMembers) {
                    data.message.add(protoField);
                }

                for (final var inheritData : inheritMap) {
                    var protoField = new ProtoField();
                    protoField.setType(new ProtoType(data.message.getName(), null));
                    protoField.setName("i" + (inheritData.inheritField.fields().size() + 1));

                    inheritData.inheritField.fields().add(protoField);
                }
            }

            proto.add(data.message);
        }

        for (final var childNode : node.children) {
            generate(childNode);
        }
    }

    private static ProtoType resolveProtoType(ProtoMessage holder, TypeInfo returnType) {
        var returnTypeNode = typeTree.get(returnType);

        if (returnType.getName().equals("Action")) {
            System.out.println(returnType.isEnum());
        }

        if (returnTypeNode != null) {
            return new ProtoType(getProtoMessageName(returnTypeNode), null);
        }

        if (generateData.containsKey(returnType)) {
            return generateData.get(returnType).getProtoType();
        }

        if (protoScalarTypes.containsKey(returnType)) {
            return protoScalarTypes.get(returnType);
        }

        if (returnType.isEnum()) {
            var enumMessage = generateGlobalEnum(returnType);
            return new ProtoType(enumMessage.getName(), null);
        }

        if (hbasePairType.isAssignableFrom(returnType)) {
            var pairMessage = generatePair(returnType);

            if (holder != null) {
                holder.add(0, pairMessage);
            }

            return new ProtoType(pairMessage.getName(), null);
        }

        if (literalExpression.equals(returnType)) {
            return new ProtoType(ProtoScalaType.STRING);
        }

        return new ProtoType(returnType.getName(), null);
    }

    private static ProtoMessage generatePair(TypeInfo pairType) {
        var t1 = pairType.getGenericTypeArguments().get(0);
        var t2 = pairType.getGenericTypeArguments().get(1);

        var name = String.format("Pair_%s_%s", t1.getName(), t2.getName());
        var message = new ProtoMessage(name);

        var t1Type = resolveProtoType(message, t1);
        var t2Type = resolveProtoType(message, t2);

        message.add(new ProtoField().setName("first").setType(t1Type));
        message.add(new ProtoField().setName("second").setType(t2Type));

        return message;
    }

    private static ProtoEnum generateGlobalEnum(TypeInfo enumType) {
        var data = new GenerateData(enumType);
        data.enumMessage = new ProtoEnum(enumType.getName());

        for (final var field : enumType.unwrap().getFields()) {
            if (Modifier.isPublic(field.getModifiers())) {
                data.enumMessage.values().add(field.getName());
            }
        }

        generateData.put(enumType, data);
        proto.add(0, data.enumMessage);

        return data.enumMessage;
    }

    private static String getProtoMessageName(TypeTreeNode node) {
        var name = node.typeInfo.getName();

        if (node.typeInfo.isInterface()) {
            return "I_" + name;
        }

        if (node.hasChildren()) {
            return "W_" + name;
        }

        return name;
    }

    private static String getProtoFieldName(MethodInfo methodInfo) {
        var name = methodInfo.getName();

        if (name.startsWith("get")) {
            name = Character.toLowerCase(name.charAt(3)) + name.substring(4);
        }

        return name;
    }

    private static String getCSharpTypeName(TypeInfo typeInfo, ProtoType type, boolean repeated) {
        var typeNode = typeTree.get(typeInfo);

        if (type == udfMapEntryProtoType && repeated) {
            return "IReadOnlyList<UDFMapEntry>";
        }

        String name;

        if (typeInfo.isInterface() || typeNode != null && typeNode.hasChildren()) {
            name = "I" + typeInfo.getName();
        } else if (type != null) {
            name = csScalarTypes.getOrDefault(type.getName(), type.getName());
        } else {
            name = typeInfo.getName();
        }

        if (repeated) {
            name = String.format("IReadOnlyList<%s>", name);
        }

        return name;
    }

    private static String getCSharpPropertyName(MethodInfo methodInfo) {
        var name = methodInfo.getName();

        if (name.startsWith("is")) {
            name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
        } else {
            name = name.substring(3);
        }

        return name;
    }

    private static boolean isPhoenixType(TypeInfo typeInfo) {
        if (typeInfo == null) {
            return false;
        }

        return typeInfo.getPackageName().startsWith("org.apache.phoenix");
    }

    private static List<MethodInfo> getProperties(TypeInfo typeInfo) {
        var properties = new ArrayList<MethodInfo>();

        for (final var methodInfo : typeInfo.getDeclaredMethods()) {
            if (methodInfo.isStatic() || !methodInfo.isPublic() || methodInfo.getParameterCount() > 0) {
                continue;
            }

            var name = methodInfo.getName();

            if (!name.startsWith("get") && !name.startsWith("is")) {
                continue;
            }

            if (methodInfo.getBaseDefinition() != null) {
                continue;
            }

            if (excludeProperties.contains(methodInfo.getFullName())) {
                continue;
            }

            properties.add(methodInfo);
        }

        return properties;
    }

    private static class GenerateData {
        public final TypeInfo type;
        public ProtoMessage message;
        public ProtoEnum enumMessage;

        public List<ProtoMember> inheritMembers;
        public ProtoFieldOneOf inheritField;

        public boolean csInterface;
        public String csTypeName;
        public List<CSharpPropertyInfo> csProperties;

        public GenerateData(TypeInfo type) {
            this.type = type;
        }

        public ProtoType getProtoType() {
            if (message != null) {
                return new ProtoType(message.getName(), null);
            }

            return new ProtoType(enumMessage.getName(), null);
        }
    }

    private static class CSharpPropertyInfo {
        public final String type;
        public final String name;

        private CSharpPropertyInfo(String type, String name) {
            this.type = type;
            this.name = name;
        }
    }
}
