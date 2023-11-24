package com.techx7.techstore.service;

import com.techx7.techstore.model.dto.user.RegisterDTO;
import com.techx7.techstore.model.dto.user.UserDTO;
import com.techx7.techstore.model.dto.user.UserProfileDTO;

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

    void editUser(UserDTO userDTO);

    UserDTO getUser(Principal principal);

    UserProfileDTO getUserProfile(Principal principal);

    void editUserProfile(UserProfileDTO userProfileDTO);

}
