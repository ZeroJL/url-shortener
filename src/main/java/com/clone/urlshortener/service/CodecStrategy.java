package com.clone.urlshortener.service;

public interface CodecStrategy {
    String BASE_58 = "Base10To58Strategy";

    String encode(long base);
    long decode(String base);
}
