package com.generator.keygenerator.sequencer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@Import(MongoSequencer.class)
class MongoSequencerTest {
    @Autowired
    private MongoSequencer mongoSequencer;
    private static final String SEQUENCE_NAME = "test_sequence";

    @Test
    void getSequence() {
        Long sequence1 = mongoSequencer.getSequence(SEQUENCE_NAME);
        assertThat(sequence1).isEqualTo(1L);

        // Another call should increment and return 2
        Long sequence2 = mongoSequencer.getSequence(SEQUENCE_NAME);
        assertThat(sequence2).isEqualTo(2L);

        // Another call should increment and return 3
        Long sequence3 = mongoSequencer.getSequence(SEQUENCE_NAME);
        assertThat(sequence3).isEqualTo(3L);
    }

    @BeforeEach
    void cleanup() {
        mongoSequencer.cleanup(SEQUENCE_NAME);
    }

}