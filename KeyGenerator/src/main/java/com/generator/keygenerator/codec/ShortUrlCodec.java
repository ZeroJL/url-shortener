package com.generator.keygenerator.codec;

import com.generator.keygenerator.codec.exception.UnknownStrategyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShortUrlCodec {

    private final Map<String, CodecStrategy> codecStrategies;

    public String encode(String strategyName, long baseNumber) {
        CodecStrategy codecStrategy = this.codecStrategies.get(strategyName);

        if (codecStrategy == null) {
            String errorMessage = "Unknown codec strategy: " + strategyName;
            log.error(errorMessage);
            throw new UnknownStrategyException(errorMessage);
        }

        return codecStrategy.encode(baseNumber);
    }
}
