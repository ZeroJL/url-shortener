package com.url.urlmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.url.urlmanager.infrastructure.repository.mongo")
@EnableCaching
public class UrlManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(UrlManagerApplication.class, args);
    }

}
