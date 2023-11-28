package com.techx7.techstore.service.impl;

import com.techx7.techstore.model.dto.user.RegisterDTO;
import com.techx7.techstore.model.dto.user.UserDTO;
import com.techx7.techstore.model.dto.user.UserProfileDTO;
import com.techx7.techstore.model.entity.Country;
import com.techx7.techstore.model.entity.Role;
import com.techx7.techstore.model.entity.User;
import com.techx7.techstore.model.entity.UserInfo;
import com.techx7.techstore.model.enums.GenderEnum;
import com.techx7.techstore.repository.CountryRepository;
import com.techx7.techstore.repository.RoleRepository;
import com.techx7.techstore.repository.UserInfoRepository;
import com.techx7.techstore.repository.UserRepository;
import com.techx7.techstore.testUtils.TestData;
import org.antlr.v4.runtime.misc.LogManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.techx7.techstore.utils.StringUtils.capitalize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private ModelMapper modelMapper;

    private MockMultipartFile mockMultipartFile;

    @Mock
    private UserInfoRepository userInfoRepository;

    @BeforeEach
    void setUp() {
        mockMultipartFile = TestData.createMultipartFile();
    }

    @Test
    void register_ValidRegisterDTO_UserSaved() {
        // Arrange
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setEmail("test@example.com");
        registerDTO.setUsername("TestUser");
        registerDTO.setPassword("password");

        User user = new User();
        user.setEmail(registerDTO.getEmail());
        user.setUsername(registerDTO.getUsername());
        user.setPassword(registerDTO.getPassword());

        when(userRepository.save(any(User.class))).thenReturn(user);

        when(modelMapper.map(registerDTO, User.class)).thenReturn(user);

        // Act
        userService.register(registerDTO);

        // Assert
        verify(userRepository, times(1)).save(any(User.class));
        verify(applicationEventPublisher, times(1)).publishEvent(any());
    }

    @Test
    void deleteAllUsers_UsersExist_UsersDeleted() {
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
    void deleteUserByUuid_ValidUuid_UserDeleted() {
        // Arrange
        UUID uuid = UUID.randomUUID();

        // Act
        userService.deleteUserByUuid(uuid);

        // Assert
        verify(userRepository, times(1)).deleteByUuid(uuid);
    }

    @Test
    void getAllUsers_UsersExist_ReturnUserDTOList() {
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
    void createAdmin_AdminCreated() {
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
    void getUserByUuid_ValidUuid_ReturnUserDTO() {
        // Arrange
        UUID uuid = UUID.randomUUID();

        User user = new User();
        user.setUuid(uuid);

        UserDTO userDTO = new UserDTO();
        userDTO.setUuid(user.getUuid());

        when(userRepository.findByUuid(uuid)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        // Act
        UserDTO result = userService.getUserByUuid(uuid);
        // Assert
        assertNotNull(result);

        assertEquals(uuid, result.getUuid());
    }

    @Test
    void editUser_ValidUserDTO_UserEdited() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setUuid(UUID.randomUUID());
        userDTO.setRoles("1,2,3");

        User user = new User();
        user.setUuid(userDTO.getUuid());

        when(userRepository.findByUuid(userDTO.getUuid())).thenReturn(Optional.of(user));
        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(new Role()));

        // Act
        userService.editUser(userDTO);

        // Assert
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @WithMockUser(username = "TestUser", roles = "USER")
    void getUserProfile_ValidPrincipal_ReturnUserProfileDTO() {
        // Arrange
        Principal principal = () -> "TestUser";
        User user = new User();
        user.setUsername("TestUser");

        UserInfo userInfo = new UserInfo();
        userInfo.setGender(GenderEnum.MALE);

        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setGender(capitalize(userInfo.getGender().name()));

        user.setUserInfo(new UserInfo());

        when(modelMapper.map(userInfo, UserProfileDTO.class)).thenReturn(userProfileDTO);

        when(userRepository.findByUsername(principal.getName())).thenReturn(Optional.of(user));

        // Act
        UserProfileDTO result = userService.getUserProfile(principal);

    }

    @Test
    void testEditUserProfile() throws IOException {
        // Arrange
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setCountry("Bulgaria");
        userProfileDTO.setImage(mockMultipartFile);
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
    void testEditUserCredentials() throws IOException {
        // Arrange
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setCountry("Bulgaria");
        userProfileDTO.setImage(mockMultipartFile);
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

    private Role getRoleEntity(String roleName) {
        Role role = new Role();
        role.setName(roleName);
        return role;
    }

}