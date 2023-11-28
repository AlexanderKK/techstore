package com.techx7.techstore.service.impl;

import com.techx7.techstore.exception.UserAlreadyActivatedException;
import com.techx7.techstore.model.entity.User;
import com.techx7.techstore.model.entity.UserActivationCode;
import com.techx7.techstore.model.events.UserRegisteredEvent;
import com.techx7.techstore.repository.UserActivationCodeRepository;
import com.techx7.techstore.repository.UserRepository;
import com.techx7.techstore.service.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.techx7.techstore.constant.Messages.USER_ALREADY_VERIFIED;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserActivationServiceImplTest {

    @Mock
    private EmailService emailService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserActivationCodeRepository userActivationCodeRepository;

    @InjectMocks
    private UserActivationServiceImpl userActivationService;

    @Test
    void testUserRegisteredThenSendRegistrationEmail() {
        // Arrange
        String userEmail = "test@example.com";
        String userName = "TestUser";

        UserRegisteredEvent event = new UserRegisteredEvent(this, userEmail, userName);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));
        when(userActivationCodeRepository.save(any(UserActivationCode.class))).thenReturn(new UserActivationCode());

        // Act
        userActivationService.userRegistered(event);

        // Assert
        verify(emailService).sendRegistrationEmail(eq(userEmail), eq(userName), anyString());
    }

    @Test
    void testCleanUpObsoleteActivationLinksThenDeleteAll() {
        // Arrange
        Integer minutesLifetime = 60;
        UserActivationCode expiredCode = new UserActivationCode();

        expiredCode.setCreated(LocalDateTime.now().minusMinutes(minutesLifetime + 1));
        when(userActivationCodeRepository.findAll()).thenReturn(List.of(expiredCode));

        // Act
        userActivationService.cleanUpObsoleteActivationLinks(minutesLifetime);

        // Assert
        verify(userActivationCodeRepository).deleteAll(anyList());
    }

    @Test
    void testCreateActivationCodeThenSaveUserActivationCode() {
        // Arrange
        String userEmail = "test@example.com";
        User user = new User();
        user.setEmail(userEmail);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));

        // Act
        String activationCode = userActivationService.createActivationCode(userEmail);

        // Assert
        assertNotNull(activationCode);

        verify(userActivationCodeRepository).save(any(UserActivationCode.class));
    }

    @Test
    void testActivateUserThenSaveUser() {
        // Arrange
        String activationCode = "testActivationCode";
        User user = new User();

        user.setUsername("TestUser");
        user.setActive(false);

        UserActivationCode userActivationCode = new UserActivationCode();
        userActivationCode.setUser(user);

        when(userActivationCodeRepository.findByActivationCode(activationCode)).thenReturn(Optional.of(userActivationCode));
        when(userRepository.findByActivationCodesIn(Set.of(userActivationCode))).thenReturn(Optional.of(user));

        // Act
        String username = userActivationService.activateUser(activationCode);

        // Assert
        assertNotNull(username);

        verify(userRepository).save(any(User.class));
    }

    @Test
    void testActivateUserWhenUserAlreadyActivatedThenThrowException() {
        // Arrange
        String activationCode = "testActivationCode";
        User user = new User();
        user.setActive(true);

        UserActivationCode userActivationCode = new UserActivationCode();
        userActivationCode.setUser(user);

        when(userActivationCodeRepository.findByActivationCode(activationCode)).thenReturn(Optional.of(userActivationCode));

        when(userRepository.findByActivationCodesIn(Set.of(userActivationCode))).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(
                UserAlreadyActivatedException.class,
                () -> userActivationService.activateUser(activationCode),
                USER_ALREADY_VERIFIED);
    }

}
