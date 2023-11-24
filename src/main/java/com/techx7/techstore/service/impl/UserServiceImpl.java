package com.techx7.techstore.service.impl;

import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.exception.PrincipalNotFoundException;
import com.techx7.techstore.model.dto.user.RegisterDTO;
import com.techx7.techstore.model.dto.user.UserDTO;
import com.techx7.techstore.model.dto.user.UserProfileDTO;
import com.techx7.techstore.model.entity.Role;
import com.techx7.techstore.model.entity.User;
import com.techx7.techstore.model.entity.UserInfo;
import com.techx7.techstore.model.events.UserRegisteredEvent;
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

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import static com.techx7.techstore.constant.Messages.*;

@Service
public class UserServiceImpl implements UserService {

    private final ModelMapper mapper;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserInfoRepository userInfoRepository;

    @Autowired
    public UserServiceImpl(ModelMapper mapper,
                           UserRepository userRepository,
                           ApplicationEventPublisher applicationEventPublisher,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           UserInfoRepository userInfoRepository) {
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.applicationEventPublisher = applicationEventPublisher;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userInfoRepository = userInfoRepository;
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
    public UserDTO getUser(Principal principal) {
        if(principal == null) {
            throw new PrincipalNotFoundException(USER_NOT_LOGGED);
        }

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "User")));

        UserDTO userDTO = mapper.map(user, UserDTO.class);

        return userDTO;
    }

    @Override
    public UserProfileDTO getUserProfile(Principal principal) {
        if(principal == null) {
            throw new PrincipalNotFoundException(USER_NOT_LOGGED);
        }

//        Country country = countryRepository.findById(101L)
//                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "Country")));
//
//
//        UserInfo userInfo = new UserInfo(
//                "Mike",
//                "Like",
//                "Pike",
//                GenderEnum.OTHER,
//                "052",
//                "Greenery",
//                "Secondary Greenery",
//                country,
//                "Okinawa", "14000"
//        );
//
//        userInfoRepository.save(userInfo);
//
//        user.setUserInfo(userInfo);
//
//        userRepository.save(user);

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
    public void editUserProfile(UserProfileDTO userProfileDTO, Principal principal) {
        if(principal == null) {
            throw new PrincipalNotFoundException(USER_NOT_LOGGED);
        }

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "User")));
    }

    private Role getRoleEntity(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "Role")));
    }

}
