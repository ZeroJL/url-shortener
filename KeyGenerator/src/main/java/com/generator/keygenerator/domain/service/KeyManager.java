package com.generator.keygenerator.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeyManager {

    private final KeyPoolMaintainer keyPoolMaintainer;

    public String getKey() {
        return keyPoolMaintainer.getKey();
    }

    public boolean isUsedKey(String key) {
        return keyPoolMaintainer.isUsedKey(key);
    }

    public void maintainUnusedKeyPool() {
        keyPoolMaintainer.maintainUnusedKeyPool();
    }
}
