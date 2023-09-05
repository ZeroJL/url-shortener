package com.clone.urlshortener.repository;

import com.clone.urlshortener.model.URLPair;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface URLPairRepository extends MongoRepository<URLPair, String> {
    Optional<URLPair> findURLPairByLongUrl(String longUrl);
}
