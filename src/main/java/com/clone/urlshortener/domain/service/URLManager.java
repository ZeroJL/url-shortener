package com.clone.urlshortener.domain.service;

import com.clone.urlshortener.codec.CodecStrategy;
import com.clone.urlshortener.codec.ShortUrlCodec;
import com.clone.urlshortener.codec.exception.UnknownStrategyException;
import com.clone.urlshortener.domain.exception.ShortUrlException;
import com.clone.urlshortener.domain.exception.ShortUrlGenerationException;
import com.clone.urlshortener.domain.model.URLPair;
import com.clone.urlshortener.infrastructure.repository.mongo.URLPairRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class URLManager {

    private final URLPairRepository urlPairRepository;
    private final KeyGeneration keyGeneration;
    private static final String PREFIX_URL = "/shorten-url/";

    public String getShortUrl(String longUrl) {
        try {
            URLPair urlPair = urlPairRepository.findURLPairByLongUrl(longUrl).orElseGet(() -> generateAndSaveNewShortUrl(longUrl));
            return urlPair.getShortUrl();
        } catch (OptimisticLockingFailureException e) {
            return fetchExistingShortUrl(longUrl);
        } catch (ShortUrlException shortUrlException) {
            handleException("Error when get short URL for: " + longUrl, shortUrlException);
        } catch (Exception e) {
            handleException("Unexpected error when get short URL for: " + longUrl, e);
        }

        throw new ShortUrlGenerationException("Failed to retrieve a short URL for: " + longUrl);
    }

    private URLPair generateAndSaveNewShortUrl(String longUrl) {
        URLPair placeHolder = createPlaceHolder(longUrl);

        String shortUrl = generateShortUrl();

        placeHolder.setShortUrl(shortUrl);
        urlPairRepository.save(placeHolder);

        return placeHolder;
    }

    private URLPair createPlaceHolder(String longUrl) {
        URLPair urlPair = new URLPair(longUrl);
        urlPairRepository.save(urlPair);

        return urlPair;
    }

    private String generateShortUrl() {
        try {
            return PREFIX_URL + keyGeneration.getKey();
        } catch (UnknownStrategyException e) {
            log.error("Generate fail because of unknown strategy", e);
            throw new ShortUrlGenerationException("Generate fail because of unknown strategy", e);
        }
    }

    private void handleException(String message, Exception e) {
        log.error(message, e);
        throw new ShortUrlException(message, e);
    }

    private String fetchExistingShortUrl(String longUrl) {
        return urlPairRepository.findURLPairByLongUrl(longUrl).orElseThrow(() ->
                new ShortUrlGenerationException("Failed to retrieve a short URL for: " + longUrl)).getShortUrl();
    }
}
