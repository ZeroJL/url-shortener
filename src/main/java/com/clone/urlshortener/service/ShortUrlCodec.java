package com.clone.urlshortener.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ShortUrlCodec {

    private final Map<String, CodecStrategy> codecStrategy;
    private final Sequencer sequencer;

    public String encode(String strategyName) {
        long sequence = sequencer.getSequence();
        return codecStrategy.get(strategyName).encode(sequence);
    }
}
