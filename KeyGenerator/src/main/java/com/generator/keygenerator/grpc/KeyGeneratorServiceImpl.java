package com.generator.keygenerator.grpc;

import io.grpc.stub.StreamObserver;
import keymanager.*;
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

    @Override
    public void isUsedKey(IsUsedKeyRequest request, StreamObserver<IsUsedKeyResponse> responseObserver) {
        boolean isUsed = checkIfKeyIsUsed(request.getShortUrlKey());
        IsUsedKeyResponse response = IsUsedKeyResponse.newBuilder().setIsUsed(isUsed).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private boolean checkIfKeyIsUsed(String key) {
        return true;
    }

    private String generateUniqueKey() {
        return "uniqueKey";
    }
}
