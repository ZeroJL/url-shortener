package com.clone.urlshortener.domain.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "used_keys")
@NoArgsConstructor
@AllArgsConstructor
public class UsedKey {
    @Id
    private String key;
}
