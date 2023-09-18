package com.generator.keygenerator.sequencer;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class LocalSequencer implements Sequencer {

    private final ConcurrentHashMap<String, AtomicLong> sequencer = new ConcurrentHashMap<>();

    @Override
    public Long getSequence(String sequenceName) {
        if (sequencer.containsKey(sequenceName)) {
            return sequencer.get(sequenceName).incrementAndGet();
        }

        AtomicLong sequence = new AtomicLong(0);
        sequencer.put(sequenceName, sequence);
        return sequence.incrementAndGet();
    }

    void setSequence(String sequenceName, long value) {
        AtomicLong sequence = new AtomicLong(value);
        sequencer.put(sequenceName, sequence);
    }
}
