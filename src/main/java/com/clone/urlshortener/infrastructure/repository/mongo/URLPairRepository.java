package com.clone.urlshortener.infrastructure.repository.mongo;

import com.clone.urlshortener.domain.model.URLPair;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface URLPairRepository extends MongoRepository<URLPair, String> {
    Optional<URLPair> findURLPairByLongUrl(String longUrl);

    Optional<URLPair> findURLPairByShortUrl(String key);
}
