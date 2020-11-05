package com.chequer.phoenixsql.service;

import com.chequer.phoenixsql.proto.Common;
import com.chequer.phoenixsql.proto.Host;
import com.chequer.phoenixsql.proto.HostServiceGrpc;
import com.chequer.phoenixsql.proto.Nodes;
import io.grpc.stub.StreamObserver;
import org.apache.phoenix.parse.BinaryParseNode;

public class HostServiceImpl extends HostServiceGrpc.HostServiceImplBase {
    @Override
    public void ping(Common.Empty request, StreamObserver<Common.Empty> responseObserver) {
        responseObserver.onNext(Common.Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void parse(Host.ParseRequest request, StreamObserver<Nodes.I_BindableStatement> responseObserver) {
        var name = Nodes.TableName.newBuilder()
                .setSchemaName("schema")
                .setTableName("table")
                .setIsSchemaNameCaseSensitive(true);

        var namedNode = Nodes.NamedTableNode.newBuilder()
                .setAlias("alias")
                .setName(name)
                .setTableSamplingRate(1.685);

        var tableNode = Nodes.W_TableNode.newBuilder()
                .setI1(namedNode);

        var multiplyParseNode = Nodes.MultiplyParseNode.newBuilder()
                .setAlias("MultiplyParseNode");

        var parseNode = Nodes.W_ParseNode.newBuilder()
                .setI1(multiplyParseNode);

        var selectStatement = Nodes.SelectStatement.newBuilder()
                .setFrom(tableNode)
                .setWhere(parseNode)
                .setHaving(parseNode);

        var bindableStatement = Nodes.I_BindableStatement.newBuilder()
                .setI27(selectStatement)
                .build();

        responseObserver.onNext(bindableStatement);
        responseObserver.onCompleted();
    }
}
