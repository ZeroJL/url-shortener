package com.generator.keygenerator.sequencer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "sequence")
public class Sequence {
    @Id
    private String id;
    @Getter
    private Long sequence;
}
