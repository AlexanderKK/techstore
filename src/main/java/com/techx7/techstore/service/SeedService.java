package com.techx7.techstore.service;

import java.io.IOException;

public interface SeedService {

    String seedAdmin();

    String seedRoles() throws IOException;

    String seedManufacturers() throws IOException;

    String seedModels();

    String seedCategories() throws IOException;

    String seedProducts() throws IOException;

    void createAdmin();

    void cleanUpDatabase();

    default void seedDatabase() throws Exception {
        System.out.println(seedRoles());
        System.out.println(seedManufacturers());
        System.out.println(seedModels());
        System.out.println(seedCategories());
        System.out.println(seedProducts());
        System.out.println(seedAdmin());
    }

    void resetAdmin();

}
