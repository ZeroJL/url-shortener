package com.clone.urlshortener.domain.service;

import com.clone.urlshortener.codec.ShortUrlCodec;
import com.clone.urlshortener.domain.model.URLPair;
import com.clone.urlshortener.domain.model.UnusedKey;
import com.clone.urlshortener.domain.model.UsedKey;
import com.clone.urlshortener.infrastructure.repository.mongo.UnusedKeyRepository;
import com.clone.urlshortener.infrastructure.repository.mongo.UsedKeyRepository;
import com.clone.urlshortener.sequencer.model.Sequence;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.scheduling.annotation.Scheduled;

import java.lang.reflect.Method;
import java.util.Optional;

import static com.clone.urlshortener.codec.CodecStrategy.BASE_58;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class KeyGenerationTest {

    @Autowired
    private KeyGeneration keyGeneration;

    @MockBean
    private UnusedKeyRepository unusedKeyRepository;
    @MockBean
    private UsedKeyRepository usedKeyRepository;
    @MockBean
    private Sequence mongoSequencer;
    @MockBean
    private ShortUrlCodec shortUrlCodec;


    @Test
    void getKey() {
        when(unusedKeyRepository.findAndRemoveTopByOrderBySequenceIdAsc()).thenReturn(Optional.of(new UnusedKey("hello", 1L)));

        String key = keyGeneration.getKey();
        verify(usedKeyRepository, times(1)).save(any(UsedKey.class));

        Assertions.assertThat(key).isEqualTo("hello");
    }

    @Test
    void generateAndSaveKey() {
        when(shortUrlCodec.encode(anyString(), anyLong()))
                .thenReturn("hello")
                .thenReturn("next")
                .thenReturn("final");
        when(usedKeyRepository.existsById("hello")).thenReturn(true);
        when(unusedKeyRepository.existsById("next")).thenReturn(true);

        keyGeneration.generateAndSaveKey();

        verify(shortUrlCodec, times(3)).encode(anyString(), anyLong());
        verify(unusedKeyRepository, times(1)).save(any(UnusedKey.class));
    }

    @Test
    void maintainUnusedKeyPool() {
        when(unusedKeyRepository.count()).thenReturn(50L);

        keyGeneration.maintainUnusedKeyPool();

        verify(unusedKeyRepository, times(50)).save(any(UnusedKey.class));

    }

    @Test
    public void scheduledAnnotation() {
        Method method;
        Scheduled scheduled;
        try {
            method = KeyGeneration.class.getDeclaredMethod("maintainUnusedKeyPool");
            scheduled = method.getAnnotation(Scheduled.class);
        } catch (NoSuchMethodException e) {
            fail("Method maintainUnusedKeyPool() does not exist");
            return;
        }

        assertNotNull(scheduled, "Method is not annotated with @Scheduled");
        assertEquals(5 * 60 * 1000, scheduled.fixedRate(), "Unexpected fixedRate value in @Scheduled annotation");
    }
}