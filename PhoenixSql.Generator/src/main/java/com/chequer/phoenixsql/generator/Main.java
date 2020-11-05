package com.chequer.phoenixsql.generator;

import com.chequer.phoenixsql.generator.proto.*;
import com.chequer.phoenixsql.generator.reflection.*;
import com.chequer.phoenixsql.generator.util.ResourceUtil;
import org.apache.phoenix.expression.LiteralExpression;
import org.apache.phoenix.parse.*;
import org.apache.phoenix.schema.PName;
import org.apache.phoenix.schema.PTableKey;
import org.apache.phoenix.schema.types.PDataType;

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
public class Main {
    private static File rootDir;
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
        put(TypeInfo.get(Object.class), new ProtoType(ProtoScalaType.STRING));
    }};

    private static final Map<String, String> csScalarTypes = new HashMap<>() {{
        put(ProtoScalaType.DOUBLE.value, "double");
        put(ProtoScalaType.FLOAT.value, "float");
        put(ProtoScalaType.INT32.value, "int");
        put(ProtoScalaType.INT64.value, "long");
        put(ProtoScalaType.BOOL.value, "bool");
        put(ProtoScalaType.STRING.value, "string");
    }};

    private static final Set<Class<?>> toRpcConvertTypes = new HashSet<>() {{
        add(PDataType.class);
        add(PName.class);
        add(LiteralExpression.class);
        add(Object.class);
        add(org.apache.hadoop.hbase.util.Pair.class);
    }};

    private static final String indent1 = "    ";
    private static final String indent2 = "        ";
    private static final String indent3 = "            ";

    private static final Map<TypeInfo, GenerateData> generateData = new HashMap<>();

    public static void main(String[] args) throws Exception {
        rootDir = new File(System.getProperty("user.dir"), "../");
        csharpNodeDir = new File(rootDir, "PhoenixSql/Proto/Nodes");
        protoFile = new File(rootDir, "proto/nodes.proto");

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

        writeJava();
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

        typeTree = TypeTree.build(types, Main::isPhoenixType);

        generate(typeTree.root);
    }

    private static void writeCSharpInterfaces() throws IOException {
        if (csharpNodeDir.exists()) {
            csharpNodeDir.delete();
        }

        csharpNodeDir.mkdir();

        for (final var data : generateData.values()) {
            if (data.csTypeName == null) {
                continue;
            }

            var csFile = Paths.get(csharpNodeDir.getCanonicalPath(), data.csTypeName + ".cs").toAbsolutePath().toString();
            var csWriter = new FileWriter(csFile);

            var typeNode = typeTree.get(data.type);
            var inheritMap = getInheritMap(typeNode, false);

            if (inheritMap != null) {
                for (final var inherit : inheritMap) {
                    for (final var inheritField : inherit.inheritMembers) {
                        data.absMessage.add(inheritField);
                    }
                }
            }

            var extended = inheritMap != null && !inheritMap.isEmpty();

            csWriter.write("namespace PhoenixSql\n");
            csWriter.write("{\n");

            if (data.message != null) {
                writeCSharpClass(data);
            }

            if (data.isInterface) {
                writeCSharpProxyClass(data);

                csWriter.write(String.format("    public interface %s", data.csTypeName));

                if (extended) {
                    csWriter.write(" : ");
                    csWriter.write(inheritMap.get(inheritMap.size() - 1).csTypeName);
                }

                csWriter.write('\n');
                csWriter.write("    {\n");

                for (int i = 0; i < data.csProperties.size(); i++) {
                    if (i > 0) {
                        csWriter.write('\n');
                    }

                    var property = data.csProperties.get(i);
                    csWriter.write(String.format("        %s %s { get; }\n", property.type, property.name));
                }

                if (data.csTypeName.equals("IBinaryParseNode")) {
                    if (data.csProperties.size() > 0) {
                        csWriter.write('\n');
                    }

                    csWriter.write("        public IParseNode LHS => Children[0];\n\n");
                    csWriter.write("        public IParseNode RHS => Children[1];\n");
                }

                csWriter.write("    }\n");
            } else {
                csWriter.write(String.format("    public partial class %s", data.csTypeName));

                if (extended) {
                    csWriter.write(" : ");
                    csWriter.write(inheritMap.get(inheritMap.size() - 1).csTypeName);
                }

                csWriter.write('\n');
                csWriter.write("    {\n");

                if (extended) {
                    for (final var baseData : inheritMap) {
                        for (final var baseProperty : baseData.csProperties) {
                            if (baseProperty.type.contains("IParseNode")) {
                                csWriter.write(String.format("        %s %s.%s => %s;\n",
                                        baseProperty.type,
                                        baseData.csTypeName,
                                        baseProperty.name,
                                        baseProperty.name));
                            }
                        }
                    }
                }

                csWriter.write("    }\n");
            }

            csWriter.write("}\n");

            csWriter.close();
        }
    }

    private static void writeCSharpClass(GenerateData data) throws IOException {
        final var template = ResourceUtil.readString("CSharpClass.txt");
        assert template != null;

        var inheritMap = getInheritMap(typeTree.get(data.type), true);

        var file = new File(csharpNodeDir, data.message.getName() + ".cs");
        var body = new StringBuilder();

        if (inheritMap != null && inheritMap.size() > 0) {
            for (final var inheritData : inheritMap) {
                for (final var inheritProperty : inheritData.csProperties) {
                    if (inheritProperty.type.contains("IParseNode")) {
                        body.append(indent2);
                        body.append(String.format("%s %s.%s => %s;\n\n",
                                inheritProperty.type,
                                inheritData.csTypeName,
                                inheritProperty.name,
                                inheritProperty.name));
                    }
                }
            }
        }

        var code = template
                .replace("[ProtoType]", data.message.getName())
                .replace("[Type]", data.type.getName())
                .replace("[Body]", body.toString().replaceAll("\\s+$", ""));

        var writer = new FileWriter(file);
        writer.write(code);
        writer.close();
    }

    private static void writeCSharpProxyClass(GenerateData data) throws IOException {
        final var template = ResourceUtil.readString("CSharpProxyClass.txt");
        assert template != null;

        var file = new File(csharpNodeDir, data.absMessage.getName() + ".cs");
        var body = new StringBuilder();

        for (final var inheritData : getInheritMap(typeTree.get(data.type), true)) {
            for (final var inheritProperty : inheritData.csProperties) {
                body.append(indent2);
                body.append(String.format("public %s %s => Message.%s;\n\n",
                        inheritProperty.type,
                        inheritProperty.name,
                        inheritProperty.name));
            }
        }

        body.append(indent2);
        body.append(String.format("public I%s Message => (I%s)inherit_;", data.type.getName(), data.type.getName()));

        var code = template
                .replace("[ProtoType]", data.absMessage.getName())
                .replace("[Type]", data.type.getName())
                .replace("[Body]", body);

        var writer = new FileWriter(file);
        writer.write(code);
        writer.close();
    }

    private static void writeJava() throws Exception {
        final var convertEnumTemplate = ResourceUtil.readString("JavaConvertEnum.txt");
        final var convertMessageTemplate = ResourceUtil.readString("JavaConvertMessage.txt");
        final var converterTemplate = ResourceUtil.readString("JavaNodeConverter.txt");

        assert convertEnumTemplate != null;
        assert convertMessageTemplate != null;
        assert converterTemplate != null;

        final var converterBody = new StringBuilder();

        for (final var data : generateData.values()) {
            var typeNode = typeTree.get(data.type);
            var typeName = getJavaSimpleTypeName(data.type.unwrap());

            var fragments = new ArrayList<String>();

            if (data.type.isEnum()) {
                var message = data.enumMessage;

                var body = new StringBuilder();
                var values = message.lastGeneratedValues;

                for (int i = 0; i < values.size() - 1; i++) {
                    var value = values.get(i);

                    body.append(indent2);

                    if (i == 0) {
                        body.append("if ");
                    } else {
                        body.append("} else if ");
                    }

                    body.append(String.format("(value == %s.%s) {\n", typeName, value));
                    body.append(indent3).append(String.format("return Nodes.%s.%s;\n", message.getName(), value));
                }

                body.append(indent2).append("}\n\n");
                body.append(indent2).append(String.format("return Nodes.%s.%s;", message.getName(), values.get(values.size() - 1)));

                fragments.add(convertEnumTemplate
                        .replace("[ProtoType]", message.getName())
                        .replace("[Type]", typeName)
                        .replace("[Body]", body.toString()));
            } else {
                var message = data.absMessage;
                boolean hasDefault = data.message != null;

                var body = new StringBuilder();

                if (data.isInterface) {
                    var fields = data.inheritField.fields();
                    boolean write = false;

                    for (int i = hasDefault ? 1 : 0; i < fields.size(); i++) {
                        var field = fields.get(i);

                        body.append(indent2);

                        if (!write) {
                            body.append("if ");
                            write = true;
                        } else {
                            body.append("} else if ");
                        }

                        body.append(String.format("(value instanceof %s) {\n", field.getName()));
                        body.append(indent3).append(String.format("builder.set%s(convert((%s) value));\n", field.getName(), field.getName()));
                    }

                    if (write) {
                        body.append(indent2).append("}");
                    }

                    if (hasDefault) {
                        if (write) {
                            body.append(" else {\n");
                        }

                        body.append(indent3).append(String.format("var defBuilder = Nodes.%s.newBuilder();\n\n", data.message.getName()));

                        writeJavaGenerateFragment(typeNode, data.message, body, fragments, "defBuilder", indent3);
                        body.append("\n\n");

                        body.append(indent3).append(String.format("builder.set%s(defBuilder);\n", data.message.getName()));
                        body.append(indent2).append("}");
                    }
                } else if (typeNode != null) {
                    writeJavaGenerateFragment(typeNode, message, body, fragments, "builder", indent2);
                } else {
                    throw new Exception();
                }

                fragments.add(convertMessageTemplate
                        .replace("[Name]", "convert")
                        .replace("[ProtoType]", message.getName())
                        .replace("[Type]", typeName)
                        .replace("[Body]", body.toString()));
            }

            for (final var fragment : fragments) {
                if (converterBody.length() > 0) {
                    converterBody.append("\n\n");
                }

                converterBody.append(fragment);
            }
        }

        var code = converterTemplate.replace("[Body]", converterBody.toString());

        var converterFile = new File(rootDir, "PhoenixSql.Host\\src\\main\\java\\com\\chequer\\phoenixsql\\util\\NodeConverter.java");
        var javaWriter = new FileWriter(converterFile);
        javaWriter.write(code);
        javaWriter.close();
    }

    private static void writeJavaGenerateFragment(
            TypeTreeNode typeNode,
            ProtoMessage message,
            StringBuilder body,
            List<String> fragments,
            String builderName,
            String indent) throws Exception {

        final var convertMessageTemplate = ResourceUtil.readString("JavaConvertMessage.txt");
        assert convertMessageTemplate != null;

        var fields = message.stream()
                .filter(m -> m instanceof ProtoField)
                .map(m -> (ProtoField) m)
                .collect(Collectors.toList());

        var map = getInheritMap(typeNode, true);
        int index = -1;
        int variable = -1;

        for (final var inheritData : map) {
            for (final var inheritProperty : inheritData.javaProperties) {
                index++;
                var protoField = fields.get(index);
                var returnType = inheritProperty.getReturnType();
                var protoMethodName = toPascalCase(protoField.getName());

                if (protoField.isRepeated()) {
                    TypeInfo elementType;
                    var converterName = "convert";

                    if (returnType.isArray()) {
                        elementType = returnType.getElementType();
                    } else {
                        var genericReturnType = TypeInfo.get((ParameterizedType) inheritProperty.getGenericReturnType());
                        elementType = genericReturnType.getGenericTypeArguments().get(0);
                    }

                    if (hbasePairType.isAssignableFrom(elementType)) {
                        var pairConvertBody = new StringBuilder();

                        var t1 = elementType.getGenericTypeArguments().get(0);
                        var t2 = elementType.getGenericTypeArguments().get(1);

                        var needConvert1 = toRpcConvertTypes.contains(t1.unwrap()) || generateData.containsKey(t1);
                        var needConvert2 = toRpcConvertTypes.contains(t2.unwrap()) || generateData.containsKey(t2);

                        var paramTypeName = String.format("Pair<%s, %s>", getJavaSimpleTypeName(t1.unwrap()), getJavaSimpleTypeName(t2.unwrap()));
                        var pairTypeName = String.format("%s.Pair_%s_%s", message.getName(), t1.getName(), t2.getName());
                        converterName = String.format("convertPair%s%s", t1.getName(), t2.getName());

                        if (needConvert1) {
                            pairConvertBody.append(indent).append(builderName).append(".setFirst(convert(value.getFirst()));\n");
                        } else {
                            pairConvertBody.append(indent).append(builderName).append(".setFirst(value.getFirst());\n");
                        }

                        if (needConvert2) {
                            pairConvertBody.append(indent).append(builderName).append(".setSecond(convert(value.getSecond()));\n");
                        } else {
                            pairConvertBody.append(indent).append(builderName).append(".setSecond(value.getSecond());");
                        }

                        fragments.add(convertMessageTemplate
                                .replace("[Name]", converterName)
                                .replace("[ProtoType]", pairTypeName)
                                .replace("[Type]", paramTypeName)
                                .replace("[Body]", pairConvertBody.toString()));
                    }

                    var needConvert = toRpcConvertTypes.contains(elementType.unwrap()) || generateData.containsKey(elementType);

                    if (needConvert) {
                        body.append(indent).append(String.format("addAll(value.%s(), NodeConverter::%s, %s::add%s);", inheritProperty.getName(), converterName, builderName, protoMethodName));
                    } else if (inheritProperty.getName().equals("getUdfParseNodes")) {
                        body.append(indent).append(String.format("addAll(value.getUdfParseNodes(), %s::addUdfParseNodes);", builderName));
                    } else {
                        throw new Exception();
                    }
                } else if (toRpcConvertTypes.contains(returnType.unwrap()) || generateData.containsKey(returnType)) {
                    var variableName = "v" + ++variable;

                    body.append(indent).append(String.format("var %s = value.%s();\n", variableName, inheritProperty.getName()));
                    body.append(indent).append(String.format("if (%s != null) %s.set%s(convert(%s));", variableName, builderName, protoMethodName, variableName));
                } else if (!returnType.unwrap().isPrimitive()) {
                    var variableName = "v" + ++variable;

                    body.append(indent).append(String.format("var %s = value.%s();\n", variableName, inheritProperty.getName()));
                    body.append(indent).append(String.format("if (%s != null) %s.set%s(%s);", variableName, builderName, protoMethodName, variableName));
                } else {
                    body.append(indent).append(String.format("%s.set%s(value.%s());", builderName, protoMethodName, inheritProperty.getName()));
                }

                if (index < fields.size() - 1) {
                    body.append('\n');
                }
            }
        }
    }

    private static String getJavaSimpleTypeName(Class<?> clazz) {
        return clazz.getCanonicalName().substring(clazz.getPackageName().length() + 1);
    }

    private static void generate(TypeTreeNode node) {
        if (node.typeInfo != null) {
            if (generateData.containsKey(node.typeInfo)) {
                return;
            }

            var data = new GenerateData(node.typeInfo);
            data.absMessage = new ProtoMessage(getProtoMessageName(node));

            generateData.put(node.typeInfo, data);

            if (!node.hasChildren()) {
                for (final var inherit : getInheritMap(node, false)) {
                    for (final var inheritField : inherit.inheritMembers) {
                        data.absMessage.add(inheritField);
                    }
                }
            }

            var protoMembers = new ArrayList<ProtoMember>();
            var csProperties = new ArrayList<CSharpPropertyInfo>();
            var javaProperties = new ArrayList<MethodInfo>();

            protoMembers.add(new ProtoSingLineComment(node.typeInfo.getFullName()));

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
                    protoType = resolveProtoType(data.absMessage, returnType);
                }

                protoField.setType(protoType);
                protoField.setName(getProtoFieldName(property));

                protoMembers.add(protoField);

                csProperties.add(new CSharpPropertyInfo(
                        getCSharpTypeName(returnType, protoType, protoField.isRepeated()),
                        getCSharpPropertyName(property)));

                javaProperties.add(property);
            }

            data.javaProperties = javaProperties;

            if (node.hasChildren()) {
                data.inheritMembers = protoMembers;

                data.isInterface = true;
                data.csTypeName = getCSharpTypeName(node.typeInfo, null, false);
                data.csProperties = csProperties;

                data.inheritField = new ProtoFieldOneOf();
                data.inheritField.setName("inherit");

                data.absMessage.add(data.inheritField);
            } else {
                data.csTypeName = data.absMessage.getName();

                for (final var protoField : protoMembers) {
                    data.absMessage.add(protoField);
                }

                for (final var inheritData : getInheritMap(node, false)) {
                    var protoField = new ProtoField();
                    protoField.setType(new ProtoType(data.absMessage.getName(), null));
                    protoField.setName(data.absMessage.getName());

                    inheritData.inheritField.fields().add(protoField);
                }
            }

            proto.add(data.absMessage);

            // --

            if (node.hasChildren() && !node.typeInfo.isAbstract()) {
                data.message = new ProtoMessage(node.typeInfo.getName());

                for (final var inheritData : getInheritMap(node, true)) {
                    for (final var inheritMember : inheritData.inheritMembers) {
                        data.message.add(inheritMember);
                    }
                }

                data.inheritField.fields().add(new ProtoField(data.message.getName(), new ProtoType(data.message.getName(), null)));

                proto.add(data.message);
            }
        }

        for (final var childNode : node.children) {
            generate(childNode);
        }
    }

    private static ProtoType resolveProtoType(ProtoMessage holder, TypeInfo returnType) {
        var returnTypeNode = typeTree.get(returnType);

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
            return "P_" + name;
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

        String name;

        if (type == udfMapEntryProtoType) {
            name = udfMapEntryProtoType.getName();
        } else if (typeInfo.isInterface() || typeNode != null && typeNode.hasChildren()) {
            name = "I" + typeInfo.getName();
        } else if (type != null) {
            name = csScalarTypes.getOrDefault(type.getName(), type.getName());
        } else {
            name = typeInfo.getName();
        }

        if (repeated) {
            if (typeInfo.unwrap() == ParseNode.class) {
                name = "System.Collections.Generic.IReadOnlyList<IParseNode>";
            } else {
                name = String.format("Google.Protobuf.Collections.RepeatedField<%s>", name);
            }
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

    private static List<GenerateData> getInheritMap(TypeTreeNode node, boolean withSelf) {
        if (node == null) {
            return null;
        }

        var inheritMap = new ArrayList<GenerateData>();

        var parentNode = node.getParent();

        while (parentNode != null) {
            inheritMap.add(0, generateData.get(parentNode.typeInfo));
            parentNode = parentNode.getParent();
        }

        if (withSelf) {
            inheritMap.add(generateData.get(node.typeInfo));
        }

        return inheritMap;
    }

    private static String toPascalCase(String value) {
        return Character.toUpperCase(value.charAt(0)) + value.substring(1);
    }

    private static class GenerateData {
        public final TypeInfo type;
        public ProtoMessage message;
        public ProtoMessage absMessage;
        public ProtoEnum enumMessage;

        public List<ProtoMember> inheritMembers;
        public ProtoFieldOneOf inheritField;

        public boolean isInterface;
        public String csTypeName;
        public List<CSharpPropertyInfo> csProperties;
        public List<MethodInfo> javaProperties;

        public GenerateData(TypeInfo type) {
            this.type = type;
        }

        public ProtoType getProtoType() {
            if (absMessage != null) {
                return new ProtoType(absMessage.getName(), null);
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
