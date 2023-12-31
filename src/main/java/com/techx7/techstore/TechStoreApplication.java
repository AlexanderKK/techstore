package com.techx7.techstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TechStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(TechStoreApplication.class, args);
    }

}
