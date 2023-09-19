package com.generator.keygenerator.sequencer;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class LocalSequencerTest {
    private final LocalSequencer localSequencer = new LocalSequencer();
    private static final String SEQUENCE_NAME = "test_sequence";

    @BeforeEach
    void beforeEach() {
        localSequencer.setSequence(SEQUENCE_NAME, 0L);
    }

    @ParameterizedTest
    @MethodSource("argumentsProvider")
    void getSequence(long base, long expect) {
        localSequencer.setSequence(SEQUENCE_NAME, base);
        Assertions.assertThat(localSequencer.getSequence(SEQUENCE_NAME)).isEqualTo(expect);
    }

    static Stream<Arguments> argumentsProvider() {
        return Stream.of(
                Arguments.of(0L, 1L),
                Arguments.of(100L, 101L),
                Arguments.of(1000L, 1001L));
    }
}