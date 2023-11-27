package com.techx7.techstore.service.impl;

import com.techx7.techstore.model.entity.User;
import com.techx7.techstore.model.entity.UserActivationCode;
import com.techx7.techstore.repository.UserActivationCodeRepository;
import com.techx7.techstore.repository.UserRepository;
import com.techx7.techstore.service.EmailService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserActivationServiceImplTestIT {

    @Autowired
    private UserActivationServiceImpl userActivationServiceToTest;

    @Autowired
    private UserActivationCodeRepository userActivationCodeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        userActivationServiceToTest =
                new UserActivationServiceImpl(emailService, userRepository, userActivationCodeRepository);

        userActivationCodeRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        userActivationCodeRepository.deleteAll();
    }

    @Test
    void testCleanUpOfActivationLinks() {
        System.out.println("Deleting expired activation codes...");

        List<UserActivationCode> activationCodes = createActivationCodes();

        userActivationCodeRepository.saveAll(activationCodes);

        assertEquals(2, userActivationCodeRepository.count());

        userActivationServiceToTest.cleanUpObsoleteActivationLinks(-1);

        assertEquals(0, userActivationCodeRepository.count());
    }

    private List<UserActivationCode> createActivationCodes() {
        return List.of(
                createActivationCode("code1"),
                createActivationCode("code2")
        );
    }

    private UserActivationCode createActivationCode(String activationCode) {
        UserActivationCode userActivationCode = new UserActivationCode();

        User user = new User();
        user.setUsername("username with " + activationCode);
        user.setEmail("user@" + activationCode);
        user.setPassword("password " + activationCode);

        userRepository.save(user);

        userActivationCode.setUser(user);
        userActivationCode.setActivationCode(activationCode);

        return userActivationCode;
    }

}
