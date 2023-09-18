package com.generator.keygenerator.infrastructure.repository.mongo;


import com.generator.keygenerator.domain.model.UnusedKey;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface UnusedKeyRepository extends MongoRepository<UnusedKey, String> {

    @Query(value = "{}", sort = "{ id : 1 }", delete = true)
    Optional<UnusedKey> findAndRemoveTopByOrderBySequenceIdAsc();
}
