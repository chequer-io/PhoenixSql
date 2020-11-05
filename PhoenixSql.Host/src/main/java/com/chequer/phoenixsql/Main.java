package com.chequer.phoenixsql;

import com.chequer.phoenixsql.proto.Handshake;
import com.chequer.phoenixsql.proto.HandshakeServiceGrpc;
import io.grpc.ManagedChannelBuilder;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        int handshakePort = Integer.parseInt(args[0]);

        try {
            var hostServer = new HostServer();
            hostServer.start();

            handshake(handshakePort, hostServer.getPort());

            System.out.println("--------------- 1");
            hostServer.waitForExit();
            System.out.println("--------------- 2");
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void handshake(int handshakePort, int hostPort) {
        var handshakeChannel = ManagedChannelBuilder
                .forAddress("localhost", handshakePort)
                .usePlaintext()
                .build();

        try {
            var handshakeService = HandshakeServiceGrpc.newBlockingStub(handshakeChannel);

            var ackRequest = Handshake.AckRequest.newBuilder()
                    .setPort(hostPort)
                    .build();

            handshakeService.ack(ackRequest);
        } finally {
            handshakeChannel.shutdown();
        }
    }
}
