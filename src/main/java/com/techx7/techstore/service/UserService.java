package com.techx7.techstore.service;

import com.techx7.techstore.model.session.TechStoreUserDetails;
import com.techx7.techstore.model.dto.user.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface UserService {

    void register(RegisterDTO registerDTO);

    void deleteAllUsers();

    void deleteUserByUuid(UUID uuid);

    List<UserDTO> getAllUsers();

    void createAdmin();

    UserDTO getUserByUuid(UUID uuid);

    void editUser(UserDTO userDTO, TechStoreUserDetails loggedUser);

    UserProfileDTO getUserProfile(Principal principal);

    void editUserProfile(UserProfileDTO userProfileDTO, Principal principal) throws IOException;

    UserCredentialsDTO getUserCredentials(Principal principal);

    void editUserCredentials(UserCredentialsDTO userCredentialsDTO, TechStoreUserDetails loggedUser);

    UserPasswordDTO getUserPassword(Principal principal);

    void editUserPassword(UserPasswordDTO userPasswordDTO, Principal principal);

}
