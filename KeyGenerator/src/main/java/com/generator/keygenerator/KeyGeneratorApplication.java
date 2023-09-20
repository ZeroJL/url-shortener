package com.generator.keygenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KeyGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(KeyGeneratorApplication.class, args);
    }

}
