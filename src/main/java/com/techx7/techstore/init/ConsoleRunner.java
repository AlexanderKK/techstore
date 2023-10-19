package com.techx7.techstore.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ConsoleRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Welcome!");

        System.out.println("Loading user roles...");

    }

}
