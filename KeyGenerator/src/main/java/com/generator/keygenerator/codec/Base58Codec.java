package com.generator.keygenerator.codec;

import org.springframework.stereotype.Component;

@Component("Base58Codec")
public class Base58Codec implements CodecStrategy {
    private static final String BASE58_ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";

    @Override
    public String encode(long base) {
        if (base == 0) {
            return String.valueOf(BASE58_ALPHABET.charAt(0));
        }
        StringBuilder encoded = new StringBuilder();
        while (base > 0) {
            int remainder = (int) (base % 58);
            encoded.insert(0, BASE58_ALPHABET.charAt(remainder));
            base /= 58;
        }
        return encoded.toString();
    }

    @Override
    public long decode(String base) {
        long decoded = 0;
        for (char character : base.toCharArray()) {
            decoded *= 58;
            decoded += BASE58_ALPHABET.indexOf(character);
        }
        return decoded;
    }

}
