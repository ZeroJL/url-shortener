package com.generator.keygenerator.grpc;

import com.generator.keygenerator.domain.service.KeyManager;
import io.grpc.stub.StreamObserver;
import keymanager.*;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class KeyGeneratorServiceImpl extends KeyGeneratorServiceGrpc.KeyGeneratorServiceImplBase {

    private final KeyManager keyManager;

    @Override
    public void generateKey(KeyRequest request, StreamObserver<KeyResponse> responseObserver) {

        try {
            String generatedKey = generateUniqueKey();
            KeyResponse response = KeyResponse.newBuilder().setKey(generatedKey).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    private String generateUniqueKey() {
        return keyManager.getKey();
    }

    @Override
    public void isUsedKey(IsUsedKeyRequest request, StreamObserver<IsUsedKeyResponse> responseObserver) {
        boolean isUsed = checkIfKeyIsUsed(request.getShortUrlKey());
        IsUsedKeyResponse response = IsUsedKeyResponse.newBuilder().setIsUsed(isUsed).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private boolean checkIfKeyIsUsed(String key) {
        return keyManager.isUsedKey(key);
    }


}
