package com.techx7.techstore.repository;

import com.techx7.techstore.model.entity.User;
import com.techx7.techstore.model.entity.UserActivationCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.techx7.techstore.testUtils.TestData.createUser;
import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {UserRepository.class, RoleRepository.class, UserActivationCodeRepository.class})
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.techx7.techstore.model.entity"})
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserActivationCodeRepository userActivationCodeRepository;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void findByEmailExistingEmail() {
        // Arrange
        User user = createUser();

        userRepository.save(user);

        // Act
        Optional<User> result = userRepository.findByEmail(user.getEmail());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user.getEmail(), result.get().getEmail());
    }

    @Test
    void findByEmailNonExistingEmail() {
        // Arrange
        String email = "nonexisting@example.com";

        // Act
        Optional<User> result = userRepository.findByEmail(email);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void findByEmailOrUsernameExistingEmail() {
        // Arrange
        User user = createUser();

        userRepository.save(user);

        // Act
        Optional<User> result = userRepository.findByEmailOrUsername(user.getEmail());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user.getEmail(), result.get().getEmail());
    }

    @Test
    void findByEmailOrUsernameExistingUsername() {
        // Arrange
        User user = createUser();

        userRepository.save(user);

        // Act
        Optional<User> result = userRepository.findByEmailOrUsername(user.getUsername());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user.getUsername(), result.get().getUsername());
    }

    @Test
    void findByEmailOrUsernameNonExistingEmailOrUsername() {
        // Arrange
        String emailOrUsername = "nonexisting";

        // Act
        Optional<User> result = userRepository.findByEmailOrUsername(emailOrUsername);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void findByActivationCodesInExistingActivationCode() {
        // Arrange
        User user = createUser();

        roleRepository.saveAll(user.getRoles());

        userRepository.save(user);

        UserActivationCode activationCode = new UserActivationCode();
        activationCode.setActivationCode("123456");
        activationCode.setUser(user);

        Set<UserActivationCode> activationCodes = new HashSet<>();
        activationCodes.add(activationCode);
        user.setActivationCodes(activationCodes);

        userActivationCodeRepository.saveAll(activationCodes);

        // Act
        Optional<User> result = userRepository.findByActivationCodesIn(activationCodes);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user.getId(), result.get().getId());
    }

    @Test
    void findByActivationCodesInNonExistingActivationCode() {
        // Arrange
        UserActivationCode activationCode = new UserActivationCode();
        activationCode.setActivationCode("123456");

        User user = createUser();

        roleRepository.saveAll(user.getRoles());

        userRepository.save(user);

        activationCode.setUser(user);

        Set<UserActivationCode> activationCodes = new HashSet<>();

        activationCodes.add(activationCode);

        userActivationCodeRepository.saveAll(activationCodes);

        // Act
        Optional<User> result = userRepository.findByActivationCodesIn(new HashSet<>());

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void findByUsernameExistingUsername() {
        // Arrange
        User user = createUser();

        userRepository.save(user);

        // Act
        Optional<User> result = userRepository.findByUsername(user.getUsername());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user.getUsername(), result.get().getUsername());
    }

    @Test
    void findByUsernameNonExistingUsername() {
        // Arrange
        String username = "nonexisting";

        // Act
        Optional<User> result = userRepository.findByUsername(username);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void deleteByUuidExistingUuid() {
        // Arrange
        User user = createUser();

        // Act
        userRepository.deleteByUuid(user.getUuid());

        // Assert
        assertFalse(userRepository.findByUuid(user.getUuid()).isPresent());
    }

    @Test
    void findByUuidExistingUuid() {
        // Arrange
        User user = createUser();

        userRepository.save(user);

        // Act
        Optional<User> result = userRepository.findByUuid(user.getUuid());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user.getUuid(), result.get().getUuid());
    }

    @Test
    void findByUuidNonExistingUuid() {
        // Arrange
        UUID uuid = UUID.randomUUID();

        // Act
        Optional<User> result = userRepository.findByUuid(uuid);

        // Assert
        assertFalse(result.isPresent());
    }

}
