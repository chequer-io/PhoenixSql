package com.chequer.phoenixsql;

import com.chequer.phoenixsql.service.HostServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class HostServer {
    private final Server server;

    public HostServer() {
        server = ServerBuilder
                .forPort(0)
                .addService(new HostServiceImpl())
                .build();
    }

    public void start() throws IOException {
        server.start();
    }

    public int getPort() {
        return server.getPort();
    }

    public void waitForExit() throws InterruptedException {
        server.awaitTermination();
    }
}
