package com.chequer.phoenixsql.service;

import com.chequer.phoenixsql.proto.Common;
import com.chequer.phoenixsql.proto.Host;
import com.chequer.phoenixsql.proto.HostServiceGrpc;
import com.chequer.phoenixsql.proto.Nodes;
import io.grpc.stub.StreamObserver;

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
                .setNamedTableNode(namedNode);

        var selectStatement = Nodes.SelectStatement.newBuilder()
                .setFrom(tableNode);

        var bindableStatement = Nodes.I_BindableStatement.newBuilder()
                .setSelectStatement(selectStatement)
                .build();

        responseObserver.onNext(bindableStatement);
        responseObserver.onCompleted();
    }
}
