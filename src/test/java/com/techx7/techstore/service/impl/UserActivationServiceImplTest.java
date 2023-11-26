package com.techx7.techstore.service.impl;

import com.techx7.techstore.model.entity.User;
import com.techx7.techstore.model.entity.UserActivationCode;
import com.techx7.techstore.repository.UserActivationCodeRepository;
import com.techx7.techstore.repository.UserRepository;
import com.techx7.techstore.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UserActivationServiceImplTest {

    private UserActivationServiceImpl userActivationService;

    @Mock
    private UserActivationCodeRepository userActivationCodeRepository;

    @Mock
    private EmailService emailService;

    private final UserRepository userRepository;

    public UserActivationServiceImplTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @BeforeEach
    void setUp() {
        userActivationService = new UserActivationServiceImpl(
                emailService,
                userRepository,
                userActivationCodeRepository
        );
    }

    @Test
    void testCleanUpOfActivationLinks() {
        List<UserActivationCode> activationCodes = createActivationCodes();

        userActivationCodeRepository.saveAll(activationCodes);

        long actualSize = userActivationCodeRepository.count();

        assertEquals(actualSize, 2L);

        userActivationService.cleanUpObsoleteActivationLinks(0);

        assertEquals(0, userActivationCodeRepository.count());
    }

    private List<UserActivationCode> createActivationCodes() {
        return List.of(
                createActivationCode("code1"),
                createActivationCode("code2")
        );
    }

    private static UserActivationCode createActivationCode(String activationCode) {
        UserActivationCode userActivationCode = new UserActivationCode();

        userActivationCode.setUser(new User());
        userActivationCode.setActivationCode(activationCode);

        return userActivationCode;
    }

}
