package com.url.urlmanager.grpc.client;

import keymanager.*;
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

    public boolean isUsedKey(String shortUrlKey) {
        IsUsedKeyRequest request = IsUsedKeyRequest.newBuilder().setShortUrlKey(shortUrlKey).build();
        IsUsedKeyResponse response = keyGeneratorServiceBlockingStub.isUsedKey(request);
        return response.getIsUsed();
    }
}
