package com.clone.urlshortener.domain.service;

import com.clone.urlshortener.codec.CodecStrategy;
import com.clone.urlshortener.codec.ShortUrlCodec;
import com.clone.urlshortener.domain.model.URLPair;
import com.clone.urlshortener.infrastructure.repository.mongo.URLPairRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class URLManager {

    private final URLPairRepository urlPairRepository;
    private final ShortUrlCodec shortUrlCodec;
    private static final String PREFIX_URL = "/shorten-url/";

    public String getShortUrl(String longUrl) {
        URLPair urlPair = urlPairRepository.findURLPairByLongUrl(longUrl).orElseGet(() -> generateAndSaveUrlPair(longUrl));
        return urlPair.getShortUrl();
    }

    private URLPair generateAndSaveUrlPair(String longUrl) {
        String shortUrl = generateShortUrl();
        URLPair urlPair = new URLPair(longUrl, shortUrl);
        urlPairRepository.save(urlPair);

        return urlPair;
    }

    private String generateShortUrl() {
        return PREFIX_URL + shortUrlCodec.encode(CodecStrategy.BASE_58);
    }


}
