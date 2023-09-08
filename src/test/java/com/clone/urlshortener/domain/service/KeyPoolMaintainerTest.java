package com.clone.urlshortener.domain.service;

import com.clone.urlshortener.codec.ShortUrlCodec;
import com.clone.urlshortener.domain.model.UnusedKey;
import com.clone.urlshortener.domain.model.UsedKey;
import com.clone.urlshortener.infrastructure.repository.mongo.UnusedKeyRepository;
import com.clone.urlshortener.infrastructure.repository.mongo.UsedKeyRepository;
import com.clone.urlshortener.sequencer.MongoSequencer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = "spring.main.allow-bean-definition-overriding=true")
class KeyPoolMaintainerTest {

    @Autowired
    private KeyPoolMaintainer keyPoolMaintainer;

    @MockBean
    private TaskScheduler taskScheduler;
    @MockBean
    private UnusedKeyRepository unusedKeyRepository;
    @MockBean
    private UsedKeyRepository usedKeyRepository;
    @MockBean
    private MongoSequencer mongoSequencer;
    @MockBean
    private ShortUrlCodec shortUrlCodec;

    @Test
    void getKey() {
        when(unusedKeyRepository.findAndRemoveTopByOrderBySequenceIdAsc()).thenReturn(Optional.of(new UnusedKey("hello", 1L)));

        String key = keyPoolMaintainer.getKey();
        verify(usedKeyRepository, times(1)).save(any(UsedKey.class));

        Assertions.assertThat(key).isEqualTo("hello");
    }

    @Test
    void generateAndSaveKey() {
        when(shortUrlCodec.encode(anyString(), anyLong())).thenReturn("uniqueKey");
        when(usedKeyRepository.existsById("uniqueKey")).thenReturn(false);
        when(unusedKeyRepository.existsById("uniqueKey")).thenReturn(false);
        when(mongoSequencer.getSequence(UnusedKey.SEQUENCE_NAME)).thenReturn(1L);
        keyPoolMaintainer.generateAndSaveKey();

        verify(unusedKeyRepository, times(1)).save(any(UnusedKey.class));
    }

    @Test
    void maintainUnusedKeyPool() {
        AtomicInteger counter = new AtomicInteger(0);
        when(shortUrlCodec.encode(anyString(), anyLong())).thenReturn("key" + counter.getAndIncrement());

        when(unusedKeyRepository.count()).thenReturn(50L);
        when(usedKeyRepository.existsById(anyString())).thenReturn(false);
        when(unusedKeyRepository.existsById(anyString())).thenReturn(false);

        keyPoolMaintainer.maintainUnusedKeyPool();

        verify(unusedKeyRepository, times(50)).save(any(UnusedKey.class));

    }

    @Test
    void scheduledAnnotation() {
        Method method;
        Scheduled scheduled;
        try {
            method = KeyManager.class.getDeclaredMethod("maintainUnusedKeyPool");
            scheduled = method.getAnnotation(Scheduled.class);
        } catch (NoSuchMethodException e) {
            fail("Method maintainUnusedKeyPool() does not exist");
            return;
        }

        assertNotNull(scheduled, "Method is not annotated with @Scheduled");
        assertEquals(5 * 60 * 1000, scheduled.fixedRate(), "Unexpected fixedRate value in @Scheduled annotation");
    }

    @Test
    void isUsedKey_usedKey() {
        when(usedKeyRepository.findById("hello")).thenReturn(Optional.of(new UsedKey("hello")));

        Assertions.assertThat(keyPoolMaintainer.isUsedKey("hello")).isTrue();
    }

    @Test
    void isUsedKey_unusedKey() {
        when(usedKeyRepository.findById("hello")).thenReturn(Optional.empty());

        Assertions.assertThat(keyPoolMaintainer.isUsedKey("hello")).isFalse();
    }
}