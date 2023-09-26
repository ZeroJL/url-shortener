package com.generator.keygenerator.grpc;

import io.grpc.stub.StreamObserver;
import keymanager.KeyGeneratorServiceGrpc;
import keymanager.KeyRequest;
import keymanager.KeyResponse;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class KeyGeneratorServiceImpl extends KeyGeneratorServiceGrpc.KeyGeneratorServiceImplBase {
    @Override
    public void generateKey(KeyRequest request, StreamObserver<KeyResponse> responseObserver) {
        String generatedKey = generateUniqueKey();
        KeyResponse response = KeyResponse.newBuilder().setKey(generatedKey).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private String generateUniqueKey() {
        return "uniqueKey";
    }
}
