package com.chequer.phoenixsql.generator.proto;

import java.io.IOException;
import java.io.Writer;
import java.util.*;

public class ProtoWriter {
    private final Writer writer;
    private int depth;
    private final Stack<IndexHandler> indexHandlerStack = new Stack<>();
    private final Map<String, Integer> enumMap = new HashMap<>();

    public ProtoWriter(Writer writer) {
        this.writer = writer;
    }

    private void appendIndent() {
        if (depth <= 0) {
            return;
        }

        try {
            char[] chars = new char[depth * 4];
            Arrays.fill(chars, ' ');

            writer.write(chars);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void append(String value) {
        try {
            writer.write(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void appendLine() {
        try {
            writer.write('\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void appendLine(String value) {
        try {
            writer.write(value + '\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(ProtoMember member) {
        if (member.getComment() != null) {
            write(member.getComment());
            appendLine();
        }

        if (member instanceof ProtoFile) {
            writeFile((ProtoFile) member);
        } else if (member instanceof ProtoMessage) {
            writeMessage((ProtoMessage) member);
        } else if (member instanceof ProtoField) {
            writeField((ProtoField) member);
        } else if (member instanceof ProtoEnum) {
            writeEnum((ProtoEnum) member);
        } else if (member instanceof ProtoSingLineComment) {
            writeSingLineComment((ProtoSingLineComment) member);
        } else if (member instanceof ProtoMultiLineComment) {
            writeMultiLineComment((ProtoMultiLineComment) member);
        }
    }

    private void writeMultiLineComment(ProtoMultiLineComment comment) {
        appendIndent();
        appendLine("/*");

        depth++;

        for (var line : comment.getContent().split("\n")) {
            appendIndent();
            appendLine(line);
        }

        depth--;

        appendIndent();
        append("*/");
    }

    private void writeSingLineComment(ProtoSingLineComment comment) {
        appendIndent();
        append(String.format("// %s", comment.getContent()));
    }

    private void writeFile(ProtoFile file) {
        boolean first = true;

        if (file.getSyntax() != null) {
            append(String.format("syntax = \"%s\";", file.getSyntax()));
            first = false;
        }

        if (file.getPackageName() != null) {
            if (!first) {
                appendLine();
                appendLine();
            } else {
                first = false;
            }

            append(String.format("package %s;", file.getPackageName()));
        }

        if (file.options().size() > 0) {
            if (!first) {
                appendLine();
                appendLine();
            } else {
                first = false;
            }

            var entries = file.options().entrySet().iterator();

            for (int i = 0; entries.hasNext(); i++) {
                if (i > 0) {
                    appendLine();
                }

                var entry = entries.next();
                append(String.format("option %s = \"%s\";", entry.getKey(), entry.getValue()));
            }
        }

        var imports = new HashSet<>(file.imports());
        imports.addAll(scanImports(file));

        if (imports.size() > 0) {
            if (!first) {
                appendLine();
                appendLine();
            }

            var importIterator = imports.iterator();

            for (int i = 0; importIterator.hasNext(); i++) {
                if (i > 0) {
                    appendLine();
                }

                append(String.format("import \"%s\";", importIterator.next()));
            }
        }

        if (file.size() > 0 && !first) {
            appendLine();
            appendLine();
        }

        writeContainer(file);
    }

    private void writeContainer(ProtoContainer container) {
        for (int i = 0; i < container.size(); i++) {
            var member = container.get(i);

            if (i > 0) {
                if (needSpace(container.get(i - 1)) || needSpace(member)) {
                    appendLine();
                }
            }

            write(member);
            appendLine();
        }
    }

    private void writeMessage(ProtoMessage message) {
        appendIndent();
        appendLine(String.format("message %s {", message.getName()));

        var indexHandler = new IndexHandler(1);

        indexHandlerStack.push(indexHandler);
        depth++;

        writeContainer(message);

        depth--;
        indexHandlerStack.pop();

        appendIndent();
        append("}");
    }

    private void writeField(ProtoField field) {
        var index = indexHandlerStack.peek();

        if (field instanceof ProtoFieldOneOf) {
            var oneOf = (ProtoFieldOneOf) field;

            appendIndent();
            appendLine(String.format("oneof %s {", oneOf.getName()));

            depth++;

            for (var member : oneOf.fields()) {
                write(member);
                appendLine();
            }

            depth--;

            appendIndent();
            append("}");
        } else {
            appendIndent();

            if (field.isRepeated()) {
                append("repeated ");
            }

            append(String.format("%s %s = %d;", field.getType().getName(), field.getName(), index.next()));
        }
    }

    private void writeEnum(ProtoEnum protoEnum) {
        appendIndent();
        appendLine(String.format("enum %s {", protoEnum.getName()));

        depth++;

        var values = protoEnum.values();
        protoEnum.lastGeneratedValues.clear();

        for (int i = 0; i < values.size(); i++) {
            var value = values.get(i);
            int enumCount = enumMap.getOrDefault(value, 0);

            if (enumCount > 0) {
                value = value + "_" + enumCount;
            }

            protoEnum.lastGeneratedValues.add(value);
            enumMap.put(value, ++enumCount);

            appendIndent();
            appendLine(String.format("%s = %d;", value, i));
        }

        depth--;

        appendIndent();
        append("}");
    }

    private boolean needSpace(ProtoMember member) {
        return member instanceof ProtoMessage ||
                member instanceof ProtoEnum ||
                member instanceof ProtoFieldOneOf;
    }

    private HashSet<String> scanImports(ProtoMember target) {
        var set = new HashSet<String>();
        var queue = new LinkedList<ProtoMember>();

        queue.push(target);

        while (queue.size() > 0) {
            var member = queue.pop();

            if (member instanceof ProtoFieldOneOf) {
                queue.addAll(((ProtoFieldOneOf) member).fields());
            } else if (member instanceof ProtoContainer) {
                for (var child : (ProtoContainer) member) {
                    queue.add(child);
                }
            } else if (member instanceof ProtoField) {
                var type = ((ProtoField) member).getType();

                if (type.getImportFile() != null) {
                    set.add(type.getImportFile());
                }
            }
        }

        return set;
    }

    private class IndexHandler {
        private int index;

        public IndexHandler(int initial) {
            index = initial;
        }

        public int next() {
            return index++;
        }
    }
}
