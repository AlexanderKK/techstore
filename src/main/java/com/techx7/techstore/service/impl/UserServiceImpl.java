package com.techx7.techstore.service.impl;

import com.techx7.techstore.exception.EmailFoundException;
import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.exception.PrincipalNotFoundException;
import com.techx7.techstore.exception.UsernameFoundException;
import com.techx7.techstore.model.dto.user.*;
import com.techx7.techstore.model.entity.*;
import com.techx7.techstore.model.events.UserRegisteredEvent;
import com.techx7.techstore.model.session.TechStoreUserDetails;
import com.techx7.techstore.repository.*;
import com.techx7.techstore.service.CloudinaryService;
import com.techx7.techstore.service.UserService;
import com.techx7.techstore.service.aop.UserLoginsAOP;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.techx7.techstore.constant.Messages.*;

@Service
public class UserServiceImpl implements UserService {

    private final static String ENTITY_NAME = "User";
    private final ModelMapper mapper;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserInfoRepository userInfoRepository;
    private final CountryRepository countryRepository;
    private final CloudinaryService cloudinaryService;
    private final UserMetadataRepository userMetadataRepository;

    @Autowired
    public UserServiceImpl(ModelMapper mapper,
                           UserRepository userRepository,
                           ApplicationEventPublisher applicationEventPublisher,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           UserInfoRepository userInfoRepository,
                           CountryRepository countryRepository,
                           CloudinaryService cloudinaryService,
                           UserMetadataRepository userMetadataRepository) {
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.applicationEventPublisher = applicationEventPublisher;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userInfoRepository = userInfoRepository;
        this.countryRepository = countryRepository;
        this.cloudinaryService = cloudinaryService;
        this.userMetadataRepository = userMetadataRepository;
    }

    @Override
    public void register(RegisterDTO registerDTO) {
        User user = mapper.map(registerDTO, User.class);

        List<UserMetadata> usersMetaData = userMetadataRepository.findAllByIp(registerDTO.getIpAddress());

        user.setUsersMetaData(usersMetaData);

        userRepository.save(user);

        applicationEventPublisher.publishEvent(
                new UserRegisteredEvent(
                        "UserService",
                        registerDTO.getEmail(),
                        registerDTO.getUsername()
                ));
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

    @UserLoginsAOP(timeInMilliseconds = 1000L)
    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> mapper.map(user, UserDTO.class))
                .toList();
    }

    @Override
    public UserDTO getUserByUuid(UUID uuid) {
        return userRepository.findByUuid(uuid)
                .map(user -> mapper.map(user, UserDTO.class))
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, ENTITY_NAME)));
    }

    @Override
    public void editUser(UserDTO userDTO, TechStoreUserDetails loggedUser) {
        User user = userRepository.findByUuid(userDTO.getUuid())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, ENTITY_NAME)));

        Set<Role> roles = getRolesSet(userDTO);

        user.setRoles(roles);

        if(loggedUser.getUsername().equals(user.getUsername())) {
            updateLoggedUserAuthorities(userDTO, loggedUser, roles);
        }

        user.editUser(userDTO);

        userRepository.save(user);
    }

    @Override
    public UserProfileDTO getUserProfile(Principal principal) {
        User user = getUserByPrincipal(principal);

        UserInfo userInfo = user.getUserInfo();

        UserProfileDTO userProfileDTO
                = userInfo == null
                ? new UserProfileDTO()
                : mapper.map(userInfo, UserProfileDTO.class);

        return userProfileDTO;
    }

    @Override
    public void editUserProfile(UserProfileDTO userProfileDTO, Principal principal) throws IOException {
        User user = getUserByPrincipal(principal);

        Country country = countryRepository.findByName(userProfileDTO.getCountry())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "Country")));

        UserInfo userInfo
                = user.getUserInfo() == null
                ? new UserInfo()
                : user.getUserInfo();

        userInfo.setCountry(country);
        userInfo.editUserProfile(userProfileDTO);

        editImageUrl(userProfileDTO, user, userInfo);

        userInfoRepository.save(userInfo);

        user.setUserInfo(userInfo);
        user.setModified(LocalDateTime.now());

        userRepository.save(user);
    }

    @Override
    public UserCredentialsDTO getUserCredentials(Principal principal) {
        User user = getUserByPrincipal(principal);

        UserCredentialsDTO userCredentialsDTO = mapper.map(user, UserCredentialsDTO.class);

        return userCredentialsDTO;
    }

    @Override
    public void editUserCredentials(UserCredentialsDTO userCredentialsDTO, TechStoreUserDetails loggedUser) {
        if(loggedUser == null) {
            throw new PrincipalNotFoundException(USER_NOT_LOGGED);
        }

        User user = userRepository.findByUsername(loggedUser.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, ENTITY_NAME)));

        // Check if email is not the same as current and email is already taken
        if(!user.getEmail().equals(userCredentialsDTO.getEmail()) &&
                userRepository.findByEmail(userCredentialsDTO.getEmail()).isPresent()) {
            throw new EmailFoundException(String.format(ENTITY_FOUND, "Email"));
        }

        // Check if username is not the same as current and username is already taken
        if(!user.getUsername().equals(userCredentialsDTO.getUsername()) &&
                userRepository.findByUsername(userCredentialsDTO.getUsername()).isPresent()) {
            throw new UsernameFoundException(String.format(ENTITY_FOUND, ENTITY_NAME));
        }

        user.editUserCredentials(userCredentialsDTO);

        loggedUser.setEmail(user.getEmail());
        loggedUser.setUsername(user.getUsername());

        userRepository.save(user);
    }

    @Override
    public UserPasswordDTO getUserPassword(Principal principal) {
        User user = getUserByPrincipal(principal);

        UserPasswordDTO userPasswordDTO = mapper.map(user, UserPasswordDTO.class);

        return userPasswordDTO;
    }

    @Override
    public void editUserPassword(UserPasswordDTO userPasswordDTO, Principal principal) {
        User user = getUserByPrincipal(principal);

        if(!passwordEncoder.matches(userPasswordDTO.getPassword(), user.getPassword())) {
            throw new EntityNotFoundException(INCORRECT_PASSWORD);
        }

        user.setPassword(passwordEncoder.encode(userPasswordDTO.getNewPassword()));
        user.setModified(LocalDateTime.now());

        userRepository.save(user);
    }

    @Override
    public void deleteByRoleNames(List<String> roleNames) {
        List<Role> roles = roleRepository.findAll()
                .stream()
                .filter(role -> roleNames.contains(role.getName()))
                .toList();

        List<User> usersToDelete = userRepository.findAll()
                .stream()
                .filter(user -> user.getRoles()
                        .stream()
                        .anyMatch(role -> roleNames.contains(role.getName()))
                ).toList();

        userRepository.deleteAll(usersToDelete);
    }

    @Override
    public boolean isUserNonActive(String emailOrUsername) {
        Optional<User> user = userRepository.findByEmailOrUsername(emailOrUsername);

        return user.isPresent() && !user.get().isActive();
    }

    @Override
    public boolean isUserRestricted(String ipAddress) {
        Optional<UserMetadata> ip = userMetadataRepository.findByIp(ipAddress);

        return ip.isPresent() && ip.get().isRestricted();
    }

    @Override
    public void createUserMetadata(UserMetadataDTO userMetadataDTO) {
        UserMetadata userMetadata = mapper.map(userMetadataDTO, UserMetadata.class);

        Optional<UserMetadata> existingUserMetadata =
                userMetadataRepository.findByIpAndUserAgent(userMetadata.getIp(), userMetadata.getUserAgent());

        if(existingUserMetadata.isEmpty()) {
            userMetadataRepository.save(userMetadata);
        }
    }

    @Override
    public boolean isUserPresent(String emailOrUsername) {
        Optional<User> user = userRepository.findByEmailOrUsername(emailOrUsername);

        return user.isPresent();
    }

    private void editImageUrl(UserProfileDTO userProfileDTO, User user, UserInfo userInfo) throws IOException {
        if(userProfileDTO.getImage().getSize() > 1) {
            String imageUrl = cloudinaryService.uploadFile(
                    userProfileDTO.getImage(),
                    user.getClass().getSimpleName(),
                    user.getUsername()
            );

            userInfo.setImageUrl(imageUrl);
        }
    }

    private static void updateLoggedUserAuthorities(UserDTO userDTO, TechStoreUserDetails loggedUser, Set<Role> roles) {
        loggedUser.setEmail(userDTO.getEmail());
        loggedUser.setUsername(userDTO.getUsername());
        loggedUser.setAuthorities(roles);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        List<GrantedAuthority> updatedAuthorities = new ArrayList<>(loggedUser.getAuthorities());

        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuthorities);

        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    private Set<Role> getRolesSet(UserDTO userDTO) {
        return Arrays.stream(userDTO.getRoles().split(","))
                .map(Long::parseLong)
                .map(roleId -> roleRepository.findById(roleId)
                        .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "Role"))))
                .map(roleDTO -> mapper.map(roleDTO, Role.class))
                .collect(Collectors.toSet());
    }

    private User getUserByPrincipal(Principal principal) {
        if(principal == null) {
            throw new PrincipalNotFoundException(USER_NOT_LOGGED);
        }

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, ENTITY_NAME)));
        return user;
    }

}
