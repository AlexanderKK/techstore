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

import java.util.Optional;

import static com.techx7.techstore.testUtils.TestData.createUser;
import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = {UserActivationCodeRepository.class, UserRepository.class})
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.techx7.techstore.model.entity"})
@DataJpaTest
class UserActivationCodeRepositoryTest {

    @Autowired
    private UserActivationCodeRepository userActivationCodeRepository;

    @Autowired
    private UserRepository userRepository;

    private UserActivationCode savedUserActivationCode;

    @BeforeEach
    void setUp() {
        userActivationCodeRepository.deleteAll();

        User user = createUser();

        userRepository.save(user);

        savedUserActivationCode = new UserActivationCode();
        savedUserActivationCode.setActivationCode("123456");
        savedUserActivationCode.setUser(user);

        savedUserActivationCode = userActivationCodeRepository.save(savedUserActivationCode);
    }

    @AfterEach
    void tearDown() {
        userActivationCodeRepository.deleteAll();
    }

    @Test
    void testFindByActivationCodeWhenUserActivationCodeExists() {
        // Arrange
        String activationCode = savedUserActivationCode.getActivationCode();

        // Act
        Optional<UserActivationCode> foundUserActivationCode = userActivationCodeRepository.findByActivationCode(activationCode);

        // Assert
        assertThat(foundUserActivationCode).isPresent();
        assertThat(foundUserActivationCode.get().getActivationCode()).isEqualTo(activationCode);
    }

    @Test
    void testFindByActivationCodeWhenUserActivationCodeDoesNotExist() {
        // Arrange
        String nonExistingActivationCode = "Non Existing Activation Code";

        // Act
        Optional<UserActivationCode> foundUserActivationCode = userActivationCodeRepository.findByActivationCode(nonExistingActivationCode);

        // Assert
        assertThat(foundUserActivationCode).isNotPresent();
    }

}
