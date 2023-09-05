package com.clone.urlshortener.service;

import com.clone.urlshortener.model.URLPair;
import com.clone.urlshortener.repository.URLPairRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.clone.urlshortener.service.CodecStrategy.BASE_58;

@Service
@RequiredArgsConstructor
public class URLManager {

    private final URLPairRepository urlPairRepository;
    private final ShortUrlCodec shortUrlCodec;

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
        return "/shorten-url" + shortUrlCodec.encode(BASE_58);
    }


}
