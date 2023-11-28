package com.techx7.techstore.service.impl;

import com.techx7.techstore.exception.EmailFoundException;
import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.exception.PrincipalNotFoundException;
import com.techx7.techstore.exception.UsernameFoundException;
import com.techx7.techstore.model.dto.user.*;
import com.techx7.techstore.model.entity.Country;
import com.techx7.techstore.model.entity.Role;
import com.techx7.techstore.model.entity.User;
import com.techx7.techstore.model.entity.UserInfo;
import com.techx7.techstore.model.enums.GenderEnum;
import com.techx7.techstore.model.session.TechStoreUserDetails;
import com.techx7.techstore.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

import static com.techx7.techstore.constant.Messages.*;
import static com.techx7.techstore.testUtils.TestData.createMultipartFile;
import static com.techx7.techstore.utils.StringUtils.capitalize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserInfoRepository userInfoRepository;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private ModelMapper mapper;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Test
    void testRegister() {
        // Arrange
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setEmail("test@example.com");
        registerDTO.setUsername("test-user");
        registerDTO.setPassword("password");

        User user = new User();
        user.setEmail(registerDTO.getEmail());
        user.setUsername(registerDTO.getUsername());
        user.setPassword(registerDTO.getPassword());

        when(userRepository.save(any(User.class))).thenReturn(user);

        when(mapper.map(registerDTO, User.class)).thenReturn(user);

        // Act
        userService.register(registerDTO);

        // Assert
        verify(userRepository, times(1)).save(any(User.class));
        verify(applicationEventPublisher, times(1)).publishEvent(any());
    }

    @Test
    void testDeleteAllUsers() {
        // Arrange
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());

        when(userRepository.findAll()).thenReturn(users);

        // Act
        userService.deleteAllUsers();

        // Assert
        verify(userRepository, times(1)).deleteAll();
    }

    @Test
    void testDeleteUserByUuid() {
        // Arrange
        UUID uuid = UUID.randomUUID();

        // Act
        userService.deleteUserByUuid(uuid);

        // Assert
        verify(userRepository, times(1)).deleteByUuid(uuid);
    }

    @Test
    void testGetAllUsers() {
        // Arrange
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());

        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<UserDTO> result = userService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(users.size(), result.size());
    }

    @Test
    void testCreateAdmin() {
        // Arrange
        User user = new User();
        user.setEmail("admin@techx7.com");
        user.setUsername("admin");
        user.setPassword("admin12345");

        when(passwordEncoder.encode(anyString())).thenReturn(user.getPassword());
        when(userRepository.save(any(User.class))).thenReturn(user);

        Role role1 = new Role();
        role1.setName("ADMIN");

        Role role2 = new Role();
        role1.setName("MANAGER");

        Role role3 = new Role();
        role1.setName("USER");

        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.of(role1));
        when(roleRepository.findByName("MANAGER")).thenReturn(Optional.of(role2));
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(role3));

        // Act
        userService.createAdmin();

        // Assert
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testGetUserByUuid() {
        // Arrange
        UUID uuid = UUID.randomUUID();

        User user = new User();
        user.setUuid(uuid);

        UserDTO userDTO = new UserDTO();
        userDTO.setUuid(user.getUuid());

        when(userRepository.findByUuid(uuid)).thenReturn(Optional.of(user));
        when(mapper.map(user, UserDTO.class)).thenReturn(userDTO);

        // Act
        UserDTO result = userService.getUserByUuid(uuid);
        // Assert
        assertNotNull(result);

        assertEquals(uuid, result.getUuid());
    }

    @Test
    @WithMockUser(username = "test-user", roles = "USER")
    void testGetUserProfileValidPrincipal() {
        // Arrange
        Principal principal = () -> "test-user";
        User user = new User();
        user.setUsername("test-user");

        UserInfo userInfo = new UserInfo();
        userInfo.setGender(GenderEnum.MALE);

        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setGender(capitalize(userInfo.getGender().name()));

        user.setUserInfo(new UserInfo());

        when(mapper.map(userInfo, UserProfileDTO.class)).thenReturn(userProfileDTO);

        when(userRepository.findByUsername(principal.getName())).thenReturn(Optional.of(user));

        // Act
        UserProfileDTO result = userService.getUserProfile(principal);
    }

    @Test
    void testEditUser() {
        UUID validUuid = UUID.randomUUID();
        UserDTO validUserDTO = new UserDTO();
        validUserDTO.setUuid(validUuid);
        validUserDTO.setEmail("test@example.com");
        validUserDTO.setUsername("testUser");
        validUserDTO.setRoles("1,2,3");

        User validUser = new User();
        validUser.setUuid(validUuid);
        validUser.setEmail("test@example.com");
        validUser.setUsername("testUser");

        when(userRepository.findByUuid(validUuid)).thenReturn(Optional.of(validUser));

        when(mapper.map(any(UserDTO.class), eq(User.class))).thenReturn(validUser);
        when(mapper.map(any(User.class), eq(UserDTO.class))).thenReturn(validUserDTO);

        // Arrange
        Set<Role> roles = new HashSet<>(Arrays.asList(new Role(), new Role(), new Role()));

        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(new Role()));

        when(mapper.map(validUserDTO, Role.class)).thenReturn(new Role());

        // Act
        userService.editUser(validUserDTO);

        // Assert
        verify(userRepository, times(1)).save(validUser);

        validUser.setRoles(roles);

        assertNotNull(validUser.getRoles());
        assertEquals(roles.size(), validUser.getRoles().size());
    }

    @Test
    void testEditUserProfile() throws IOException {
        // Arrange
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setCountry("Bulgaria");
        userProfileDTO.setImage(createMultipartFile());
        userProfileDTO.setGender("Other");

        Principal principal = mock(Principal.class);
        User user = new User();

        UserInfo userInfo = new UserInfo();

        when(userRepository.findByUsername(principal.getName())).thenReturn(Optional.of(user));
        when(userInfoRepository.save(any(UserInfo.class))).thenReturn(userInfo);
        when(countryRepository.findByName(userProfileDTO.getCountry())).thenReturn(Optional.of(new Country()));

        // Act
        assertDoesNotThrow(() -> userService.editUserProfile(userProfileDTO, principal));

        // Assert
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testEditUserCredentialsThrowPrincipalNotFoundException() {
        // Arrange
        UserCredentialsDTO userCredentialsDTO = new UserCredentialsDTO();
        userCredentialsDTO.setEmail("test@example.com");
        userCredentialsDTO.setUsername("test-user");

        TechStoreUserDetails loggedUser = null;

        // Act and Assert
        PrincipalNotFoundException exception = assertThrows(PrincipalNotFoundException.class, () -> userService.editUserCredentials(userCredentialsDTO, loggedUser));

        assertEquals(USER_NOT_LOGGED, exception.getMessage());
    }

    @Test
    void testEditUserCredentialsThrowEmailFoundException() {
        // Arrange
        UserCredentialsDTO userCredentialsDTO = new UserCredentialsDTO();
        userCredentialsDTO.setEmail("test@example.com");
        userCredentialsDTO.setUsername("test-user");

        TechStoreUserDetails loggedUser = mock(TechStoreUserDetails.class);
        when(loggedUser.getUsername()).thenReturn("test-user");

        User user = new User();
        user.setEmail("test@example.bg");
        user.setUsername("test-user");

        when(userRepository.findByUsername(loggedUser.getUsername())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(userCredentialsDTO.getEmail())).thenReturn(Optional.of(user));

        // Act and Assert
        EmailFoundException exception = assertThrows(
                EmailFoundException.class,
                () -> userService.editUserCredentials(userCredentialsDTO, loggedUser)
        );

        assertEquals(String.format(ENTITY_FOUND, "Email"), exception.getMessage());
    }

    @Test
    void testEditUserCredentialsThrowUsernameFoundException() {
        // Arrange
        UserCredentialsDTO userCredentialsDTO = new UserCredentialsDTO();
        userCredentialsDTO.setEmail("test@example.com");
        userCredentialsDTO.setUsername("another-user");

        TechStoreUserDetails loggedUser = mock(TechStoreUserDetails.class);
        when(loggedUser.getUsername()).thenReturn("test-user");

        User user = new User();
        user.setEmail("test@example.com");
        user.setUsername("test-user");

        when(userRepository.findByUsername(loggedUser.getUsername())).thenReturn(Optional.of(user));
        when(userRepository.findByUsername(userCredentialsDTO.getUsername())).thenReturn(Optional.of(user));

        // Act and Assert
        UsernameFoundException exception = assertThrows(
                UsernameFoundException.class,
                () -> userService.editUserCredentials(userCredentialsDTO, loggedUser)
        );

        assertEquals(String.format(ENTITY_FOUND, "User"), exception.getMessage());
    }


    @Test
    void testEditUserCredentials() throws IOException {
        // Arrange
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setCountry("Bulgaria");
        userProfileDTO.setImage(createMultipartFile());
        userProfileDTO.setGender("Other");

        Principal principal = mock(Principal.class);
        User user = new User();

        when(userRepository.findByUsername(principal.getName())).thenReturn(Optional.of(user));
        when(countryRepository.findByName(userProfileDTO.getCountry())).thenReturn(Optional.of(new Country()));

        // Act
        assertDoesNotThrow(() -> userService.editUserProfile(userProfileDTO, principal));

        // Assert
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testEditPassword() {
        Principal principal = () -> "test-user";

        User user = new User();
        user.setUsername("test-user");
        user.setPassword(passwordEncoder.encode("password"));

        UserPasswordDTO userPasswordDTO = new UserPasswordDTO();
        userPasswordDTO.setPassword("password");
        userPasswordDTO.setNewPassword("new-password");

        when(userRepository.findByUsername(principal.getName())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(userPasswordDTO.getNewPassword())).thenReturn("encoded-new-password");

        // Arrange
        when(passwordEncoder.matches(userPasswordDTO.getPassword(), user.getPassword())).thenReturn(true);

        // Act
        userService.editUserPassword(userPasswordDTO, principal);

        // Assert
        verify(userRepository, times(1)).save(user);

        assertEquals("encoded-new-password", user.getPassword());
    }

    @Test
    void testEditPasswordThrowEntityNotFound() {
        Principal principal = () -> "test-user";

        User user = new User();
        user.setUsername("test-user");
        user.setPassword(passwordEncoder.encode("password"));

        UserPasswordDTO userPasswordDTO = new UserPasswordDTO();
        userPasswordDTO.setPassword("password");
        userPasswordDTO.setNewPassword("new-password");

        when(userRepository.findByUsername(principal.getName())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(userPasswordDTO.getNewPassword())).thenReturn("encoded-new-password");

        // Arrange
        UserPasswordDTO emptyUserPasswordDTO = new UserPasswordDTO();
        emptyUserPasswordDTO.setPassword("");
        emptyUserPasswordDTO.setNewPassword("");

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.editUserPassword(emptyUserPasswordDTO, principal)
        );

        assertEquals(INCORRECT_PASSWORD, exception.getMessage());
    }

}
