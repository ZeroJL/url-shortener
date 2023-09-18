package com.generator.keygenerator.codec;

public interface CodecStrategy {
    String BASE_58 = "Base58Codec";

    String encode(long base);
    long decode(String base);
}
