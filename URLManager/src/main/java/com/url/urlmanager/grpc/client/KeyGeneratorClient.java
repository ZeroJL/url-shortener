package com.url.urlmanager.grpc.client;

import keymanager.KeyGeneratorServiceGrpc;
import keymanager.KeyRequest;
import keymanager.KeyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KeyGeneratorClient {

    private final KeyGeneratorServiceGrpc.KeyGeneratorServiceBlockingStub keyGeneratorServiceBlockingStub;

    public String generateKey() {
        KeyRequest request = KeyRequest.newBuilder().build();
        KeyResponse response = keyGeneratorServiceBlockingStub.generateKey(request);
        return response.getKey();
    }
}
