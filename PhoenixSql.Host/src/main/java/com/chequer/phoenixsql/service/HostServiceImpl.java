package com.chequer.phoenixsql.service;

import com.chequer.phoenixsql.proto.Common;
import com.chequer.phoenixsql.proto.Host;
import com.chequer.phoenixsql.proto.HostServiceGrpc;
import com.chequer.phoenixsql.proto.Nodes;
import com.chequer.phoenixsql.util.NodeConverter;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.apache.phoenix.exception.PhoenixParserException;
import org.apache.phoenix.parse.PhoenixSQLParser;
import org.apache.phoenix.parse.SQLParser;

public class HostServiceImpl extends HostServiceGrpc.HostServiceImplBase {
    @Override
    public void ping(Common.Empty request, StreamObserver<Common.Empty> responseObserver) {
        responseObserver.onNext(Common.Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void parse(Host.ParseRequest request, StreamObserver<Nodes.I_BindableStatement> responseObserver) {
        try {
            var parser = new SQLParser(request.getSql());
            var statement = parser.parseStatement();
            var rpcStatement = NodeConverter.convert(statement).build();

            responseObserver.onNext(rpcStatement);
            responseObserver.onCompleted();
        } catch (PhoenixParserException e) {
            responseObserver.onError(Status.ABORTED
                    .withDescription(e.getMessage())
                    .asRuntimeException());
        } catch (Throwable t) {
            responseObserver.onError(Status.UNKNOWN
                    .withDescription(t.getMessage())
                    .asRuntimeException());
        }
    }
}
