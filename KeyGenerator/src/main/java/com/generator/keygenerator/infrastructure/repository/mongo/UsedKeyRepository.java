package com.generator.keygenerator.infrastructure.repository.mongo;


import com.generator.keygenerator.domain.model.UsedKey;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsedKeyRepository extends MongoRepository<UsedKey, String> {
}
