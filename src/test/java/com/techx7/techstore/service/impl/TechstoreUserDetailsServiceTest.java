package com.techx7.techstore.service.impl;

import com.techx7.techstore.exception.UserNotActivatedException;
import com.techx7.techstore.model.entity.Role;
import com.techx7.techstore.model.entity.User;
import com.techx7.techstore.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static com.techx7.techstore.testUtils.TestData.createUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TechstoreUserDetailsServiceTest {

    private TechstoreUserDetailsServiceImpl serviceToTest;

    @Mock
    private UserRepository mockUserRepository;

    @BeforeEach
    void setUp() {
        serviceToTest = new TechstoreUserDetailsServiceImpl(mockUserRepository);
    }

    @Test
    void testUserNotFound() {
        assertThrows(
                UsernameNotFoundException.class,
                () -> serviceToTest.loadUserByUsername("test@example.com")
        );
    }

    @Test
    void testUserNotActivated() {
        when(mockUserRepository.findByEmailOrUsername("test@example.com"))
                .thenReturn(Optional.of(new User()));

        assertThrows(
                UserNotActivatedException.class,
                () -> serviceToTest.loadUserByUsername("test@example.com")
        );
    }

    @Test
    void testUserFound() {
        // Arrange
        User testUser = createUser();

        when(mockUserRepository.findByEmailOrUsername("test@example.com"))
                .thenReturn(Optional.of(testUser));

        // Act
        UserDetails userDetails = serviceToTest.loadUserByUsername(testUser.getEmail());

        // Assert
        assertNotNull(userDetails);

        assertEquals(
                testUser.getUsername(),
                userDetails.getUsername(),
                "Username not populated properly"
        );

        assertEquals(userDetails.getPassword(), testUser.getPassword());

        assertTrue(containsAuthority(userDetails, "ROLE_" + getUserRoleName(testUser)));
    }

    private static String getUserRoleName(User testUser) {
        return testUser.getRoles().stream().findFirst().map(Role::getName).orElse(null);
    }

    private static boolean containsAuthority(UserDetails userDetails, String expectedAuthority) {
        return userDetails.getAuthorities()
                .stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(expectedAuthority));
    }

}
