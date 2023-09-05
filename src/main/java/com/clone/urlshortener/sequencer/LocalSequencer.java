package com.clone.urlshortener.sequencer;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class LocalSequencer implements Sequencer {

    private final AtomicLong sequencer = new AtomicLong(0);
    @Override
    public long getSequence() {
        return sequencer.incrementAndGet();
    }

    void setSequencer(long value) {
        sequencer.set(value);
    }
}
