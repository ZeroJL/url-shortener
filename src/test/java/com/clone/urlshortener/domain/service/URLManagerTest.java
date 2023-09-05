package com.clone.urlshortener.domain.service;

import com.clone.urlshortener.codec.CodecStrategy;
import com.clone.urlshortener.domain.model.URLPair;
import com.clone.urlshortener.codec.ShortUrlCodec;
import com.clone.urlshortener.infrastructure.repository.mongo.URLPairRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class URLManagerTest {

    @InjectMocks
    private URLManager urlManager;
    @Mock
    private URLPairRepository urlPairRepository;
    @Mock
    private ShortUrlCodec shortUrlCodec;

    @Test
    void getShortUrl_existingLongUrl() {
        String longUrl = "https://example.com";
        URLPair expect = new URLPair(longUrl, "exist");

        when(urlPairRepository.findURLPairByLongUrl(longUrl)).thenReturn(Optional.of(expect));

        String result = urlManager.getShortUrl(longUrl);
        Assertions.assertThat(result).isEqualTo("exist");

        verify(urlPairRepository, never()).save(any());
    }

    @Test
    void getShortUrl_newLongUrl() {
        String longUrl = "https://example.com";

        when(urlPairRepository.findURLPairByLongUrl(longUrl)).thenReturn(Optional.empty());
        when(shortUrlCodec.encode(CodecStrategy.BASE_58)).thenReturn("hello");
        String result = urlManager.getShortUrl(longUrl);
        Assertions.assertThat(result).isEqualTo("/shorten-url/hello");

        verify(urlPairRepository).save(any(URLPair.class));
    }

}