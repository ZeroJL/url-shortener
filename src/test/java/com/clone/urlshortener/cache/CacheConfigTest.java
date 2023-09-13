package com.clone.urlshortener.cache;

import com.clone.urlshortener.domain.model.URLPair;
import com.clone.urlshortener.domain.service.URLManager;
import com.clone.urlshortener.infrastructure.repository.mongo.URLPairRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class CacheConfigTest {

    @Autowired
    private URLManager urlManager;
    @Autowired
    private CacheManager cacheManager;

    @MockBean
    private URLPairRepository urlPairRepository;

    @Test
    public void testCachingBehavior() {
        when(urlPairRepository.findURLPairByShortUrl("sampleShortUrl")).thenReturn(Optional.of(new URLPair("https://github.com/ZeroJL/url-shortener")));
        String longUrl = urlManager.getLongUrl("sampleShortUrl");

        Cache urlsCache = cacheManager.getCache("urls");
        String cachedLongUrl = urlsCache.get("sampleShortUrl", String.class);

        assertEquals(longUrl, cachedLongUrl);
    }

}