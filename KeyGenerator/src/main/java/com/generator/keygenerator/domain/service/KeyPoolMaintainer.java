package com.generator.keygenerator.domain.service;


import com.generator.keygenerator.codec.ShortUrlCodec;
import com.generator.keygenerator.domain.model.UnusedKey;
import com.generator.keygenerator.domain.model.UsedKey;
import com.generator.keygenerator.infrastructure.repository.mongo.UnusedKeyRepository;
import com.generator.keygenerator.infrastructure.repository.mongo.UsedKeyRepository;
import com.generator.keygenerator.sequencer.Sequencer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import static com.generator.keygenerator.codec.CodecStrategy.BASE_58;

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

    private String generateKey() {
        SecureRandom random = new SecureRandom();
        String generatedKey = shortUrlCodec.encode(BASE_58, random.nextLong());

        while (usedKeyRepository.existsById(generatedKey) || unusedKeyRepository.existsById(generatedKey)) {
            generatedKey = shortUrlCodec.encode(BASE_58, random.nextLong());
        }

        return generatedKey;
    }

    public void maintainUnusedKeyPool() {
        long unusedKeyCount = unusedKeyRepository.count();

        List<UnusedKey> unusedKeys = new ArrayList<>();
        while (unusedKeyCount < MIN_UNUSED_KEYS) {
            String key = generateKey();
            unusedKeys.add(new UnusedKey(key, mongoSequencer.getSequence(UnusedKey.SEQUENCE_NAME)));
            unusedKeyCount++;
        }

        unusedKeyRepository.saveAll(unusedKeys);
    }
}
