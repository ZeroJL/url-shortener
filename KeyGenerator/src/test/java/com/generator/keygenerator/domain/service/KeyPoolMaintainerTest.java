package com.generator.keygenerator.domain.service;

import com.generator.keygenerator.codec.ShortUrlCodec;
import com.generator.keygenerator.domain.model.UnusedKey;
import com.generator.keygenerator.domain.model.UsedKey;
import com.generator.keygenerator.infrastructure.repository.mongo.UnusedKeyRepository;
import com.generator.keygenerator.infrastructure.repository.mongo.UsedKeyRepository;
import com.generator.keygenerator.sequencer.MongoSequencer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

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
    void maintainUnusedKeyPool() {
        AtomicInteger counter = new AtomicInteger(0);
        when(shortUrlCodec.encode(anyString(), anyLong())).thenReturn("key" + counter.getAndIncrement());

        when(unusedKeyRepository.count()).thenReturn(50L);
        when(usedKeyRepository.existsById(anyString())).thenReturn(false);
        when(unusedKeyRepository.existsById(anyString())).thenReturn(false);

        keyPoolMaintainer.maintainUnusedKeyPool();

        verify(unusedKeyRepository, times(1)).saveAll(any(List.class));
    }

    @Test
    void scheduledAnnotation() {
        Method method;
        Scheduled scheduled;
        try {
            method = KeyPoolMaintainer.class.getDeclaredMethod("maintainUnusedKeyPool");
            scheduled = method.getAnnotation(Scheduled.class);
        } catch (NoSuchMethodException e) {
            fail("Method maintainUnusedKeyPool() does not exist");
            return;
        }

        assertNotNull(scheduled, "Method is not annotated with @Scheduled");
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