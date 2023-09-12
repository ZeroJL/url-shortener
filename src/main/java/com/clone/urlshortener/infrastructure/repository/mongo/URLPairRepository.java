package com.clone.urlshortener.infrastructure.repository.mongo;

import com.clone.urlshortener.domain.model.URLPair;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface URLPairRepository extends MongoRepository<URLPair, String> {
    Optional<URLPair> findURLPairByLongUrl(String longUrl);

    Optional<URLPair> findURLPairByShortUrl(String key);

    @Query(value = "{'shortUrl': ?0}", delete = true)
    Optional<URLPair> findAndRemoveByShortUrl(String shortUrl);
}
