package com.techx7.techstore.service.impl;

import com.google.gson.Gson;
import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.model.dto.category.ImportCategoryJsonDTO;
import com.techx7.techstore.model.dto.manufacturer.ImportManufacturerJsonDTO;
import com.techx7.techstore.model.dto.model.ImportModelJsonDTO;
import com.techx7.techstore.model.dto.product.ImportProductJsonDTO;
import com.techx7.techstore.model.dto.role.ImportRoleJsonDTO;
import com.techx7.techstore.model.entity.*;
import com.techx7.techstore.repository.*;
import com.techx7.techstore.service.CloudinaryService;
import com.techx7.techstore.service.SeedService;
import com.techx7.techstore.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.techx7.techstore.constant.Messages.*;
import static com.techx7.techstore.constant.Paths.*;

@Service
public class SeedServiceImpl implements SeedService {

    private final Gson gson;
    private final ModelMapper mapper;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;
    private final PasswordEncoder passwordEncoder;
    private final ManufacturerRepository manufacturerRepository;
    private final ModelRepository modelRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final UserService userService;
    private final UserMetadataRepository userMetadataRepository;

    @Autowired
    public SeedServiceImpl(Gson gson,
                           ModelMapper mapper,
                           RoleRepository roleRepository,
                           UserRepository userRepository,
                           CloudinaryService cloudinaryService,
                           PasswordEncoder passwordEncoder,
                           ManufacturerRepository manufacturerRepository,
                           ModelRepository modelRepository,
                           CategoryRepository categoryRepository,
                           ProductRepository productRepository,
                           UserService userService,
                           UserMetadataRepository userMetadataRepository) {
        this.gson = gson;
        this.mapper = mapper;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.cloudinaryService = cloudinaryService;
        this.passwordEncoder = passwordEncoder;
        this.manufacturerRepository = manufacturerRepository;
        this.modelRepository = modelRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.userService = userService;
        this.userMetadataRepository = userMetadataRepository;
    }

    @Override
    public String seedRoles() throws IOException {
        System.out.println("Importing user roles...");

        if(roleRepository.count() != 0) {
            return String.format(ENTITIES_ALREADY_SEEDED, "Roles");
        }

        BufferedReader bufferedReader = readFileFromResources(IMPORT_ROLES_JSON_PATH);

        ImportRoleJsonDTO[] roleJsonDTOs = gson.fromJson(bufferedReader, ImportRoleJsonDTO[].class);

        List<Role> roles = Arrays.stream(roleJsonDTOs)
                .map(roleJsonDTO -> mapper.map(roleJsonDTO, Role.class))
                .toList();

        for (Role role : roles) {
            String imageUrl = cloudinaryService.seedFile(
                    role.getClass().getSimpleName(), role.getName()
            );

            role.setImageUrl(imageUrl);
        }

        roleRepository.saveAll(roles);

        return String.format(ENTITIES_SEEDED_SUCCESSFULLY, "Roles");
    }

    @Override
    public String seedAdmin() {
        System.out.println("Importing admin...");

        if(userRepository.count() != 0) {
            return String.format(ENTITIES_ALREADY_SEEDED, "Admin");
        }

        createAdmin();

        return String.format(ENTITIES_SEEDED_SUCCESSFULLY, "Admin");
    }

    @Override
    public String seedManufacturers() throws IOException {
        System.out.println("Importing manufacturers...");

        if(manufacturerRepository.count() != 0) {
            return String.format(ENTITIES_ALREADY_SEEDED, "Manufacturers");
        }

        BufferedReader bufferedReader = readFileFromResources(IMPORT_MANUFACTURERS_JSON_PATH);

        ImportManufacturerJsonDTO[] manufacturerJsonDTOs = gson.fromJson(bufferedReader, ImportManufacturerJsonDTO[].class);

        List<Manufacturer> manufacturers = Arrays.stream(manufacturerJsonDTOs)
                .map(manufacturerJsonDTO -> mapper.map(manufacturerJsonDTO, Manufacturer.class))
                .toList();

        for (Manufacturer manufacturer : manufacturers) {
            String imageUrl = cloudinaryService.seedFile(
                    manufacturer.getClass().getSimpleName(), manufacturer.getName()
            );

            manufacturer.setImageUrl(imageUrl);
        }

        manufacturerRepository.saveAll(manufacturers);

        return String.format(ENTITIES_SEEDED_SUCCESSFULLY, "Manufacturers");
    }

    @Override
    public String seedModels() {
        System.out.println("Importing models...");

        if(modelRepository.count() != 0) {
            return String.format(ENTITIES_ALREADY_SEEDED, "Models");
        }

        BufferedReader bufferedReader = readFileFromResources(IMPORT_MODELS_JSON_PATH);

        ImportModelJsonDTO[] modelJsonDTOs = gson.fromJson(bufferedReader, ImportModelJsonDTO[].class);

        List<Model> models = Arrays.stream(modelJsonDTOs)
                .map(modelJsonDTO -> mapper.map(modelJsonDTO, Model.class))
                .toList();

        modelRepository.saveAll(models);

        return String.format(ENTITIES_SEEDED_SUCCESSFULLY, "Models");
    }

    @Override
    public String seedCategories() throws IOException {
        System.out.println("Importing categories...");

        if(categoryRepository.count() != 0) {
            return String.format(ENTITIES_ALREADY_SEEDED, "Categories");
        }

        BufferedReader bufferedReader = readFileFromResources(IMPORT_CATEGORIES_JSON_PATH);

        ImportCategoryJsonDTO[] categoryJsonDTOs = gson.fromJson(bufferedReader, ImportCategoryJsonDTO[].class);

        List<Category> categories = Arrays.stream(categoryJsonDTOs)
                .map(categoryJsonDTO -> mapper.map(categoryJsonDTO, Category.class))
                .toList();

        for (Category category : categories) {
            String imageUrl = cloudinaryService.seedFile(
                    category.getClass().getSimpleName(), category.getName()
            );

            category.setImageUrl(imageUrl);
        }

        categoryRepository.saveAll(categories);

        return String.format(ENTITIES_SEEDED_SUCCESSFULLY, "Categories");
    }

    @Override
    public String seedProducts() throws IOException {
        System.out.println("Importing products...");

        if(productRepository.count() != 0) {
            return String.format(ENTITIES_ALREADY_SEEDED, "Products");
        }

        BufferedReader bufferedReader = readFileFromResources(IMPORT_PRODUCTS_JSON_PATH);

        ImportProductJsonDTO[] productJsonDTOs = gson.fromJson(bufferedReader, ImportProductJsonDTO[].class);

        List<Product> products = Arrays.stream(productJsonDTOs)
                .map(productJsonDTO -> mapper.map(productJsonDTO, Product.class))
                .toList();

        for (Product product : products) {
            String imageUrl = cloudinaryService.seedFile(
                    product.getClass().getSimpleName(),
                    product.getModel().getManufacturer().getName() + " " + product.getModel().getName()
            );

            product.setImageUrl(imageUrl);
        }

        productRepository.saveAll(products);

        return String.format(ENTITIES_SEEDED_SUCCESSFULLY, "Products");
    }

    @Override
    public void cleanUpDatabase() {
        System.out.println("Cleaning up database...");

        productRepository.deleteAll();
        categoryRepository.deleteAll();
        modelRepository.deleteAll();
        manufacturerRepository.deleteAll();

        System.out.printf(ENTITIES_CLEANED_UP_SUCCESSFULLY + System.lineSeparator(), "Entities");
    }

    @Override
    public void createAdmin() {
        User user = new User();

        user.setEmail("admin@techx7.com");
        user.setUsername("admin");
        user.setPassword(
                passwordEncoder.encode("admin12345")
        );

        user.setRoles(Set.of(
                getRoleEntity("ADMIN"),
                getRoleEntity("MANAGER"),
                getRoleEntity("USER")
        ));

        user.setActive(true);

//        UserMetadata userMetadata = new UserMetadata();
//        userMetadata.setIp("0.0.0.0");
//        userMetadata.setUserAgent("Admin agent");
//
//        userMetadataRepository.save(userMetadata);
//
//        user.setUsersMetaData(List.of(userMetadata));

        userRepository.save(user);
    }

    @Override
    public void resetAdmin() {
        System.out.println("Resetting admin...");

        userService.deleteByRoleNames(List.of("ADMIN"));

        createAdmin();

        System.out.printf(ENTITY_RESET_SUCCESSFULLY + System.lineSeparator(), "Admin");
    }

    private Role getRoleEntity(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "Role")));
    }

    private static BufferedReader readFileFromResources(String fileName) {
        return new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(
                                ClassLoader.getSystemResourceAsStream(fileName))));
    }

}
