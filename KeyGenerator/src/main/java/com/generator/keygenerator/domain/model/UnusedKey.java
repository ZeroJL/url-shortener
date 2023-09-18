package com.generator.keygenerator.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "unused_keys")
@NoArgsConstructor
@AllArgsConstructor
public class UnusedKey {
    public static final String SEQUENCE_NAME = "unused_key_sequence";

    @Id @Getter
    private String key;
    @Setter
    private Long sequenceId;
}
