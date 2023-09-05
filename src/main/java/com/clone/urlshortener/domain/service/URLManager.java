package com.clone.urlshortener.domain.service;

import com.clone.urlshortener.codec.CodecStrategy;
import com.clone.urlshortener.codec.ShortUrlCodec;
import com.clone.urlshortener.codec.exception.UnknownStrategyException;
import com.clone.urlshortener.domain.exception.DuplicateShortUrlException;
import com.clone.urlshortener.domain.exception.ShortUrlException;
import com.clone.urlshortener.domain.exception.ShortUrlGenerationException;
import com.clone.urlshortener.domain.model.URLPair;
import com.clone.urlshortener.infrastructure.repository.mongo.URLPairRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class URLManager {

    private final URLPairRepository urlPairRepository;
    private final ShortUrlCodec shortUrlCodec;
    private static final String PREFIX_URL = "/shorten-url/";

    public String getShortUrl(String longUrl) {
        try {
            URLPair urlPair = urlPairRepository.findURLPairByLongUrl(longUrl).orElseGet(() -> generateAndSaveUrlPair(longUrl));
            return urlPair.getShortUrl();
        } catch (DataAccessException dataAccessException) {
            handleException("Database error when get short URL for: " + longUrl, dataAccessException);
        } catch (ShortUrlException shortUrlException) {
            handleException("Error when get short URL for: " + longUrl, shortUrlException);
        } catch (Exception e) {
            handleException("Unexpected error when get short URL for: " + longUrl, e);
        }

        throw new ShortUrlGenerationException("Failed to retrieve a short URL for: " + longUrl);
    }

    private URLPair generateAndSaveUrlPair(String longUrl) {
        String shortUrl = generateShortUrl();
        URLPair urlPair = new URLPair(longUrl, shortUrl);
        try {
            urlPairRepository.save(urlPair);
        } catch (DataIntegrityViolationException e) {
            log.error("Duplicate long URL.", e);
            throw new DuplicateShortUrlException("Duplicate long URL.", e);
        }

        return urlPair;
    }

    private String generateShortUrl() {
        try {
            return PREFIX_URL + shortUrlCodec.encode(CodecStrategy.BASE_58);
        } catch (UnknownStrategyException e) {
            log.error("Generate fail because of unknown strategy", e);
            throw new ShortUrlGenerationException("Generate fail because of unknown strategy", e);
        }
    }

    private void handleException(String message, Exception e) {
        log.error(message, e);
        throw new ShortUrlException(message, e);
    }
}
