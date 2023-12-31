package com.techx7.techstore.testUtils;

import com.techx7.techstore.model.dto.user.UserDTO;
import com.techx7.techstore.model.entity.*;
import com.techx7.techstore.model.enums.GenderEnum;
import com.techx7.techstore.model.multipart.MultipartFileImpl;
import com.techx7.techstore.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.techx7.techstore.constant.Paths.RESOURCES_IMAGES_DIRECTORY;

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

    @Autowired(required = false)
    private ModelMapper mapper;

    @Autowired(required = false)
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    public void cleanAllTestData() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        modelRepository.deleteAll();
        manufacturerRepository.deleteAll();
        roleRepository.deleteAll();
        userActivationCodeRepository.deleteAll();
        userRepository.deleteAll();
    }

    public List<UserDTO> createAndSaveUsers() {
        User user1 = createUser();

        User user2 = new User();

        user2.setEmail("test2@example.com");
        user2.setUsername("test-user2");
        user2.setPassword("test-pass2");
        user2.setRoles(user1.getRoles());

        user2.setPassword(passwordEncoder.encode(user2.getPassword()));

        List<User> users = List.of(user1, user2);

        roleRepository.saveAll(user1.getRoles());

        userRepository.saveAll(users);

        return users.stream().map(user -> mapper.map(user, UserDTO.class)).toList();
    }

    public User createAndSaveUser() {
        User user = getUser();

        Set<Role> roles = new HashSet<>(roleRepository.saveAll(user.getRoles()));

        user.setRoles(roles);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public UserInfo createAndSaveUserInfo() {
        UserInfo userInfo = new UserInfo();

        userInfo.setImageUrl("test.png");
        userInfo.setFirstName("John");
        userInfo.setLastName("Doe");
        userInfo.setPhoneNumber("0887654321");
        userInfo.setGender(GenderEnum.MALE);
        userInfo.setAddress("Test Address");
        userInfo.setCountry(countryRepository.findById(1L).orElse(null));
        userInfo.setCity("Test City");
        userInfo.setZipCode("1000");

        return userInfoRepository.save(userInfo);
    }

    public void createUserActivationCode(String activationCode, boolean isActive) {
        cleanAllTestData();

        UserActivationCode userActivationCode = new UserActivationCode();
        userActivationCode.setActivationCode(activationCode);

        User user = new User();

        user.setEmail("test@example.com");
        user.setUsername("test-user");
        user.setPassword("test-pass");
        user.setRoles(Set.of(createAndSaveRole()));

        if(isActive) {
            user.setActive(true);
        }

        user.setActivationCodes(Set.of(userActivationCode));

        userActivationCode.setUser(user);

        userRepository.save(user);

        userActivationCodeRepository.save(userActivationCode);
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

    public Role createAndSaveRole() {
        Role role = createRole();

        return roleRepository.save(role);
    }

    public static Role createRole() {
        Role role = new Role();
        role.setName("USER");
        role.setImageUrl("testRole.png");

        return role;
    }

    public Product createAndSaveProduct() {
        return productRepository.save(createProduct());
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

        product.setSpecification("OS: Windows XP");

        return product;
    }

    public Model createAndSaveModel() {
        return modelRepository.save(createModel());
    }

    public Model createModel() {
        Model model = new Model();

        model.setName("Test Model");

        model.setProducts(new HashSet<>());

        Manufacturer manufacturer = createAndSaveManufacturer();

        model.setManufacturer(manufacturer);

        return model;
    }

    public Manufacturer createAndSaveManufacturer() {
        Manufacturer manufacturer = new Manufacturer();

        manufacturer.setName("test-manufacturer");
        manufacturer.setImageUrl("test.png");

        return manufacturerRepository.save(manufacturer);
    }

    public List<Category> createAndSaveCategories() {
        return categoryRepository.saveAll(createCategories());
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

    public static MultipartFile createValidMultipartImage() throws IOException {
        FileInputStream inputStream = new FileInputStream(RESOURCES_IMAGES_DIRECTORY + "favicon.png");

        return new MultipartFileImpl(
                "Image", "favicon.png", "image/png", inputStream);
    }

}
