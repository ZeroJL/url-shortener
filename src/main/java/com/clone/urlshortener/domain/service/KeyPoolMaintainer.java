package com.clone.urlshortener.domain.service;

import com.clone.urlshortener.codec.ShortUrlCodec;
import com.clone.urlshortener.domain.model.UnusedKey;
import com.clone.urlshortener.domain.model.UsedKey;
import com.clone.urlshortener.infrastructure.repository.mongo.UnusedKeyRepository;
import com.clone.urlshortener.infrastructure.repository.mongo.UsedKeyRepository;
import com.clone.urlshortener.sequencer.Sequencer;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

import static com.clone.urlshortener.codec.CodecStrategy.BASE_58;

@Service
@RequiredArgsConstructor
public class KeyPoolMaintainer {

    private static final int MIN_UNUSED_KEYS = 100;

    private final UnusedKeyRepository unusedKeyRepository;
    private final UsedKeyRepository usedKeyRepository;
    private final Sequencer mongoSequencer;
    private final ShortUrlCodec shortUrlCodec;

    public String getKey() {
        UnusedKey unusedKey = unusedKeyRepository.findAndRemoveTopByOrderBySequenceIdAsc()
                .orElseThrow(() -> new RuntimeException("No unused keys available"));

        usedKeyRepository.save(new UsedKey(unusedKey.getKey()));

        return unusedKey.getKey();
    }

    public boolean isUsedKey(String key) {
        return usedKeyRepository.findById(key).isPresent();
    }

    public void generateAndSaveKey() {
        SecureRandom random = new SecureRandom();
        String generatedKey = shortUrlCodec.encode(BASE_58, random.nextLong());

        while (usedKeyRepository.existsById(generatedKey) || unusedKeyRepository.existsById(generatedKey)) {
            generatedKey = shortUrlCodec.encode(BASE_58, random.nextLong());
        }
        saveNewKey(generatedKey);
    }


    private UnusedKey saveNewKey(String key) {
        UnusedKey unusedKey = new UnusedKey(key, mongoSequencer.getSequence(UnusedKey.SEQUENCE_NAME));
        return unusedKeyRepository.save(unusedKey);
    }

    public void maintainUnusedKeyPool() {
        long unusedKeyCount = unusedKeyRepository.count();

        while (unusedKeyCount < MIN_UNUSED_KEYS) {
            generateAndSaveKey();
            unusedKeyCount++;
        }
    }
}
