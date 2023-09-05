package com.clone.urlshortener.service;

import com.clone.urlshortener.model.URLPair;
import com.clone.urlshortener.repository.URLPairRepository;
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

        String result = urlManager.getShortUrl(longUrl);
        Assertions.assertThat(result).isEqualTo("hello");

        verify(urlPairRepository).save(any(URLPair.class));
    }

}