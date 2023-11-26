package com.techx7.techstore.service.impl;

import com.techx7.techstore.exception.UserNotActivatedException;
import com.techx7.techstore.model.entity.Role;
import com.techx7.techstore.model.entity.User;
import com.techx7.techstore.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TechstoreUserDetailsServiceImplTest {

    private TechstoreUserDetailsServiceImpl serviceToTest;

    @Mock
    private UserRepository mockUserRepository;

    @BeforeEach
    void setUp() {
        serviceToTest = new TechstoreUserDetailsServiceImpl(mockUserRepository);
    }

    @Test
    void testMock() {
        User user = new User();
        user.setUsername("Mike");

        when(mockUserRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        Optional<User> userOpt = mockUserRepository.findByEmail("test@example.com");

        User userEntity = userOpt.get();

        Assertions.assertEquals(userEntity.getUsername(), "Mike");
    }

    @Test
    void testUserNotFound() {
        assertThrows(
                UsernameNotFoundException.class,
                () -> serviceToTest.loadUserByUsername("mike@gmail.com")
        );
    }

    @Test
    void testUserNotActivated() {
        when(mockUserRepository.findByEmailOrUsername("mike@gmail.com"))
                .thenReturn(Optional.of(new User()));

        assertThrows(
                UserNotActivatedException.class,
                () -> serviceToTest.loadUserByUsername("mike@gmail.com")
        );
    }

    @Test
    void testUserFound() {
        // Arrange
        User testUser = createTestUser();

        when(mockUserRepository.findByEmailOrUsername("mike@gmail.com"))
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

    private static User createTestUser() {
        User user = new User();
        user.setEmail("mike@gmail.com");
        user.setUsername("mike");
        user.setPassword("mike1234");
        user.setRoles(Set.of(createTestRole()));
        user.setActive(true);

        return user;
    }

    private static Role createTestRole() {
        Role role = new Role();
        role.setName("USER");

        return role;
    }

}
