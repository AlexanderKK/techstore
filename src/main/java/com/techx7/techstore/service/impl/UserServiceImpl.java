package com.techx7.techstore.service.impl;

import com.techx7.techstore.config.TechStoreUserDetails;
import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.exception.PrincipalNotFoundException;
import com.techx7.techstore.model.dto.user.*;
import com.techx7.techstore.model.entity.Country;
import com.techx7.techstore.model.entity.Role;
import com.techx7.techstore.model.entity.User;
import com.techx7.techstore.model.entity.UserInfo;
import com.techx7.techstore.model.events.UserRegisteredEvent;
import com.techx7.techstore.repository.CountryRepository;
import com.techx7.techstore.repository.RoleRepository;
import com.techx7.techstore.repository.UserInfoRepository;
import com.techx7.techstore.repository.UserRepository;
import com.techx7.techstore.service.UserService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.techx7.techstore.constant.Messages.*;
import static com.techx7.techstore.util.FileUtils.saveFileLocally;

@Service
public class UserServiceImpl implements UserService {

    private final ModelMapper mapper;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserInfoRepository userInfoRepository;
    private final CountryRepository countryRepository;

    @Autowired
    public UserServiceImpl(ModelMapper mapper,
                           UserRepository userRepository,
                           ApplicationEventPublisher applicationEventPublisher,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           UserInfoRepository userInfoRepository,
                           CountryRepository countryRepository) {
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.applicationEventPublisher = applicationEventPublisher;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userInfoRepository = userInfoRepository;
        this.countryRepository = countryRepository;
    }

    @Override
    public void register(RegisterDTO registerDTO) {
        User user = mapper.map(registerDTO, User.class);

        userRepository.save(user);

        applicationEventPublisher.publishEvent(new UserRegisteredEvent(
                "UserService",
                registerDTO.getEmail(),
                registerDTO.getUsername())
        );
    }

    @Override
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    @Override
    @Transactional
    public void deleteUserByUuid(UUID uuid) {
        userRepository.deleteByUuid(uuid);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> mapper.map(user, UserDTO.class))
                .toList();
    }

    @Override
    public void createAdmin() {
        User user = new User();

        user.setEmail("admin@techx7.com");
        user.setUsername("admin");
        user.setPassword(
                passwordEncoder.encode("admin12345")
        );

        user.setRoles(Set.of(
                getRoleEntity("ADMIN"),
                getRoleEntity("MANAGER"),
                getRoleEntity("USER")
        ));

        user.setActive(true);

        userRepository.save(user);
    }

    @Override
    public UserDTO getUserByUuid(UUID uuid) {
        return userRepository.findByUuid(uuid)
                .map(user -> mapper.map(user, UserDTO.class))
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "User")));
    }

    @Override
    public void editUser(UserDTO userDTO) {
        User user = userRepository.findByUuid(userDTO.getUuid())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "User")));

        user.setRoles(
                Arrays.stream(userDTO.getRoles().split(","))
                        .map(Long::parseLong)
                        .map(roleId -> roleRepository.findById(roleId)
                                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "Role"))))
                        .map(roleDTO -> mapper.map(roleDTO, Role.class))
                        .collect(Collectors.toSet())
        );

        user.editUser(userDTO);

        userRepository.save(user);
    }

    @Override
    public UserProfileDTO getUserProfile(Principal principal) {
        if(principal == null) {
            throw new PrincipalNotFoundException(USER_NOT_LOGGED);
        }

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "User")));

        UserInfo userInfo = user.getUserInfo();

        UserProfileDTO userProfileDTO
                = userInfo == null
                ? new UserProfileDTO()
                : mapper.map(userInfo, UserProfileDTO.class);

        return userProfileDTO;
    }

    @Override
    public void editUserProfile(UserProfileDTO userProfileDTO, Principal principal) throws IOException {
        if(principal == null) {
            throw new PrincipalNotFoundException(USER_NOT_LOGGED);
        }

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "User")));

        Country country = countryRepository.findByName(userProfileDTO.getCountry())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "Country")));

        UserInfo userInfo
                = user.getUserInfo() == null
                ? new UserInfo()
                : user.getUserInfo();

        saveFileLocally(userProfileDTO.getImage());

        userInfo.setCountry(country);
        userInfo.editUserProfile(userProfileDTO);

        userInfoRepository.save(userInfo);

        user.setUserInfo(userInfo);
        user.setModified(LocalDateTime.now());

        userRepository.save(user);
    }

    @Override
    public UserCredentialsDTO getUserCredentials(Principal principal) {
        if(principal == null) {
            throw new PrincipalNotFoundException(USER_NOT_LOGGED);
        }

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "User")));

        UserCredentialsDTO userCredentialsDTO = mapper.map(user, UserCredentialsDTO.class);

        return userCredentialsDTO;
    }

    @Override
    public void editUserCredentials(UserCredentialsDTO userCredentialsDTO, TechStoreUserDetails loggedUser) {
        if(loggedUser == null) {
            throw new PrincipalNotFoundException(USER_NOT_LOGGED);
        }

        User user = userRepository.findByUsername(loggedUser.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "User")));

        user.editUserCredentials(userCredentialsDTO);

        loggedUser.setUsername(user.getUsername());

        userRepository.save(user);
    }

    @Override
    public UserPasswordDTO getUserPassword(Principal principal) {
        if(principal == null) {
            throw new PrincipalNotFoundException(USER_NOT_LOGGED);
        }

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "User")));

        UserPasswordDTO userPasswordDTO = mapper.map(user, UserPasswordDTO.class);

        return userPasswordDTO;
    }

    @Override
    public void editUserPassword(UserPasswordDTO userPasswordDTO, Principal principal) {
        if(principal == null) {
            throw new PrincipalNotFoundException(USER_NOT_LOGGED);
        }

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "User")));

        if(!passwordEncoder.matches(userPasswordDTO.getPassword(), user.getPassword())) {
            throw new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "Password"));
        }

        user.setPassword(passwordEncoder.encode(userPasswordDTO.getNewPassword()));

        userRepository.save(user);
    }

    private Role getRoleEntity(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "Role")));
    }

}
