package com.techx7.techstore.testUtils;

import com.techx7.techstore.model.entity.*;
import com.techx7.techstore.repository.*;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
public class TestData {

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private UserActivationCodeRepository userActivationCodeRepository;

    public void cleanAllTestData() {
        roleRepository.deleteAll();
        userRepository.deleteAll();
        userActivationCodeRepository.deleteAll();
    }

    public static User createUser() {
        User user = getUser();

        return user;
    }

    private static User getUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setUsername("test-user");
        user.setPassword("test-pass");
        user.setRoles(Set.of(createRole()));
        user.setActive(true);

        return user;
    }

    public static Role createRole() {
        Role role = new Role();
        role.setName("USER");
        role.setImageUrl("testRole.png");

        return role;
    }

    public Role createRoleAndSave() {
        Role role = createRole();

        return roleRepository.save(role);
    }

    public UserActivationCode createUserActivationCode(String activationCode, boolean isActive) {
        cleanAllTestData();

        UserActivationCode userActivationCode = new UserActivationCode();
        userActivationCode.setActivationCode(activationCode);

        User user = new User();

        user.setEmail("test@example.com");
        user.setUsername("test-user");
        user.setPassword("test-pass");
        user.setRoles(Set.of(createRoleAndSave()));

        if(isActive) {
            user.setActive(true);
        }

        user.setActivationCodes(Set.of(userActivationCode));

        userActivationCode.setUser(user);

        userRepository.save(user);

        return userActivationCodeRepository.save(userActivationCode);
    }

    public User createAndSaveUser() {
        User user = getUser();

        Set<Role> roles = new HashSet<>(roleRepository.saveAll(user.getRoles()));

        user.setRoles(roles);

        return userRepository.save(user);
    }

    public static MockMultipartFile createMultipartFile() {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "test.png",
                MediaType.IMAGE_PNG_VALUE,
                "TestImage".getBytes()
        );

        return file;
    }

    public Model createModel() {
        Model model = new Model();

        model.setName("Test Model");
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setName("manufacturer-name");

        model.setProducts(new HashSet<>());
        model.setUuid(UUID.randomUUID());

        manufacturerRepository.save(manufacturer);

        model.setManufacturer(manufacturer);

        return model;
    }

    private Set<Category> createCategories() {
        Category category1 = new Category();
        category1.setImageUrl("category1.png");
        category1.setName("Category1");

        Category category2 = new Category();
        category2.setImageUrl("category2.png");
        category2.setName("Category2");

        Set<Category> categories = Set.of(category1, category2);

        return categories;
    }

    public Product createProduct() {
        Product product = new Product();

        // Set model
        Model model = createModel();
        product.setModel(model);

        modelRepository.save(model);

        // Set categories
        Set<Category> categories = createCategories();
        product.setCategories(categories);

        categoryRepository.saveAll(categories);

        // Set other product characteristics
        product.setImageUrl("test.png");

        product.setPrice(BigDecimal.TEN);

        product.setInitialQuantity(15);

        product.setAvailableQuantity(15);

        product.setSpecification("Test Specification");

        return product;
    }

}
