package com.url.urlmanager.grpc.config;

import keymanager.KeyGeneratorServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfig {

    @GrpcClient("KeyGenerator")
    private KeyGeneratorServiceGrpc.KeyGeneratorServiceBlockingStub keyGeneratorServiceBlockingStub;

    @Bean
    public KeyGeneratorServiceGrpc.KeyGeneratorServiceBlockingStub keyGeneratorServiceBlockingStub() {
        return keyGeneratorServiceBlockingStub;
    }

}
