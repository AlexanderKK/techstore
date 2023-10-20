package com.techx7.techstore.init;

import com.techx7.techstore.service.SeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ConsoleRunner implements CommandLineRunner {

    private final SeedService seedService;

    @Autowired
    public ConsoleRunner(SeedService seedService) {
        this.seedService = seedService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Welcome to Tech x7!");

        System.out.println("Importing user roles...");
        System.out.println(seedService.seedRoles());
    }

}
