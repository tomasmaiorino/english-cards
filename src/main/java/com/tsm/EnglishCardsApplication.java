package com.tsm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing(modifyOnCreate = false)
public class EnglishCardsApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnglishCardsApplication.class, args);
    }
}
