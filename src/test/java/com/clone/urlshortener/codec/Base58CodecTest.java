package com.clone.urlshortener.codec;

import com.clone.urlshortener.codec.Base58Codec;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class Base58CodecTest {

    private final Base58Codec base58Codec = new Base58Codec();

    @ParameterizedTest
    @MethodSource("argumentsProvider")
    void encode(long number, String str) {
        String actual = base58Codec.encode(number);
        Assertions.assertThat(actual).isEqualTo(str);
    }

    @ParameterizedTest
    @MethodSource("argumentsProvider")
    void decode(long number, String str) {
        long actual = base58Codec.decode(str);
        Assertions.assertThat(actual).isEqualTo(number);
    }

    static Stream<Arguments> argumentsProvider() {
        return Stream.of(
                Arguments.of(2468135791013L, "27qMi57J")
        );
    }
}