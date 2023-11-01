package com.url.urlmanager.domain.service;

import com.url.urlmanager.domain.exception.ExpiredShortUrlException;
import com.url.urlmanager.domain.exception.ShortUrlException;
import com.url.urlmanager.domain.exception.ShortUrlGenerationException;
import com.url.urlmanager.domain.model.URLPair;
import com.url.urlmanager.grpc.client.KeyGeneratorClient;
import com.url.urlmanager.infrastructure.repository.mongo.URLPairRepository;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class URLManager {

    private final URLPairRepository urlPairRepository;
    private final KeyGeneratorClient keyGeneratorClient;
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

    public URLPair generateAndSaveNewShortUrl(String longUrl) {
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
            return PREFIX_URL + keyGeneratorClient.generateKey();
        } catch (StatusRuntimeException e) {
            Status status = e.getStatus();
            switch (status.getCode()) {
                case DEADLINE_EXCEEDED:
                    log.error("Deadline exceeded for gRPC call", e);
                    break;
                case UNAVAILABLE:
                    log.error("gRPC service unavailable", e);
                    break;
                default:
                    log.error("gRPC call failed", e);
            }
            throw new ShortUrlGenerationException("Failed to generate short URL due to gRPC error", e);
        } catch (Exception e) {
            log.error("Failed to generate short URL", e);
            throw new ShortUrlGenerationException("Failed to generate short URL", e);
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

    @Cacheable(value = "urls", key = "#shortUrl")
    public String getLongUrl(String shortUrl) {
        Optional<URLPair> urlPair = urlPairRepository.findURLPairByShortUrl(shortUrl);
        if (urlPair.isPresent()) {
            return urlPair.get().getLongUrl();
        }

        try {
            if (keyGeneratorClient.isUsedKey(shortUrl)) {
                throw new ExpiredShortUrlException("Expired short URL: " + shortUrl);
            }
        } catch (Exception e) {
            log.error("Error checking if key is used", e);
            throw new ShortUrlException("Failed to check if key is used for shortUrl: " + shortUrl, e);
        }

        throw new ShortUrlException("URLPair not found for shortUrl: " + shortUrl);
    }

    @CacheEvict(value = "urls", key = "#shortUrl")
    public URLPair deleteUrl(String shortUrl) {

        return urlPairRepository.findAndRemoveByShortUrl(shortUrl)
                .orElseThrow(() -> new ShortUrlException("URLPair not found for shortUrl: " + shortUrl));
    }
}
