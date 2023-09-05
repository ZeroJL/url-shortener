package com.clone.urlshortener.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class Base10To58StrategyTest {

    private final Base10To58Strategy base10To58Strategy = new Base10To58Strategy();

    @ParameterizedTest
    @MethodSource("argumentsProvider")
    void encode(long number, String str) {
        String actual = base10To58Strategy.encode(number);
        Assertions.assertThat(actual).isEqualTo(str);
    }

    @ParameterizedTest
    @MethodSource("argumentsProvider")
    void decode(long number, String str) {
        long actual = base10To58Strategy.decode(str);
        Assertions.assertThat(actual).isEqualTo(number);
    }

    static Stream<Arguments> argumentsProvider() {
        return Stream.of(
                Arguments.of(2468135791013L, "27qMi57J")
        );
    }
}