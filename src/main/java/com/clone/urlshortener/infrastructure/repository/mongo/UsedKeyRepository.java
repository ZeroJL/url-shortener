package com.clone.urlshortener.infrastructure.repository.mongo;

import com.clone.urlshortener.domain.model.UsedKey;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsedKeyRepository extends MongoRepository<UsedKey, String> {
}
