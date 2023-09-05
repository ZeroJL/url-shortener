package com.clone.urlshortener.service;

public interface CodecStrategy {
    String encode(long base);
    long decode(String base);
}
