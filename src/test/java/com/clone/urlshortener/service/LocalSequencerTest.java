package com.clone.urlshortener.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class LocalSequencerTest {
    private final LocalSequencer localSequencer = new LocalSequencer();

    @BeforeEach
    void beforeEach() {
        localSequencer.setSequencer(0L);
    }

    @ParameterizedTest
    @MethodSource("argumentsProvider")
    void getSequence(long base, long expect) {
        localSequencer.setSequencer(base);
        Assertions.assertThat(localSequencer.getSequence()).isEqualTo(expect);
    }

    static Stream<Arguments> argumentsProvider() {
        return Stream.of(
                Arguments.of(0L, 1L),
                Arguments.of(100L, 101L),
                Arguments.of(1000L, 1001L));
    }
}