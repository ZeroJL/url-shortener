package com.clone.urlshortener.domain.service;

import com.clone.urlshortener.domain.exception.ShortUrlException;
import com.clone.urlshortener.domain.model.URLPair;
import com.clone.urlshortener.infrastructure.repository.mongo.URLPairRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.OptimisticLockingFailureException;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class URLManagerTest {

    @InjectMocks
    private URLManager urlManager;
    @Mock
    private URLPairRepository urlPairRepository;
    @Mock
    private KeyManager keyManager;

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
        when(keyManager.getKey()).thenReturn("hello");
        String result = urlManager.getShortUrl(longUrl);
        Assertions.assertThat(result).isEqualTo("/shorten-url/hello");

        verify(urlPairRepository, times(2)).save(any(URLPair.class));
    }

    @Test
    void getShortUrl_optimisticLockingFailure() {
        String longUrl = "https://example.com";

        when(urlPairRepository.findURLPairByLongUrl(longUrl))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(new URLPair(longUrl, "/shorten-url/hello")));

        doThrow(OptimisticLockingFailureException.class)
                .when(urlPairRepository).save(any(URLPair.class));

        String result = urlManager.getShortUrl(longUrl);
        Assertions.assertThat(result).isEqualTo("/shorten-url/hello");

        verify(urlPairRepository, times(2)).findURLPairByLongUrl(longUrl);
        verify(urlPairRepository).save(any(URLPair.class));
    }

    @Test
    void deleteShortUrl_existShortUrl() {
        String shortUrl = "hello";
        when(urlPairRepository.findAndRemoveByShortUrl(shortUrl))
                .thenReturn(Optional.of(new URLPair("/shorten-url/hello", "hello")));

        URLPair urlPair = urlManager.deleteUrl(shortUrl);

        Assertions.assertThat(urlPair.getLongUrl()).isEqualTo("/shorten-url/hello");
        Assertions.assertThat(urlPair.getShortUrl()).isEqualTo("hello");
    }

    @Test
    void deleteShortUrl_newShortUrl() {
        String shortUrl = "hello";
        when(urlPairRepository.findAndRemoveByShortUrl(shortUrl))
                .thenThrow(new ShortUrlException("URLPair not found for shortUrl: " + shortUrl));


        Throwable thrown = catchThrowable(() -> urlManager.deleteUrl(shortUrl));
        Assertions.assertThat(thrown)
                .isInstanceOf(ShortUrlException.class)
                .hasMessage("URLPair not found for shortUrl: " + shortUrl);

    }

}