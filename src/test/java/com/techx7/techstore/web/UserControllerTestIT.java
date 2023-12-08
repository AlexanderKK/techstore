package com.techx7.techstore.web;

import com.techx7.techstore.model.dto.user.UserCredentialsDTO;
import com.techx7.techstore.model.dto.user.UserDTO;
import com.techx7.techstore.model.dto.user.UserPasswordDTO;
import com.techx7.techstore.model.dto.user.UserProfileDTO;
import com.techx7.techstore.model.entity.Role;
import com.techx7.techstore.model.entity.User;
import com.techx7.techstore.model.session.TechStoreUserDetails;
import com.techx7.techstore.repository.RoleRepository;
import com.techx7.techstore.repository.UserRepository;
import com.techx7.techstore.testUtils.TestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import static com.techx7.techstore.constant.Messages.ENTITY_FOUND;
import static com.techx7.techstore.constant.Messages.INCORRECT_PASSWORD;
import static com.techx7.techstore.testUtils.TestData.createMultipartFile;
import static com.techx7.techstore.testUtils.TestData.createUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestData testData;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        testData.cleanAllTestData();
        userRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        testData.cleanAllTestData();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "test-admin", roles = {"USER", "ADMIN"})
    void testGetUsersAddsUsersToModel() throws Exception {
        testData.createAndSaveUsers();

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("users"))
                .andExpect(view().name("users/users"));
    }

    @Test
    @WithMockUser(username = "test-admin", roles = {"USER", "ADMIN"})
    void testGetUserAddsUserAndRolesToModel() throws Exception {
        User user = testData.createAndSaveUser();

        UUID uuid = user.getUuid();

        mockMvc.perform(get("/users/edit/{uuid}", uuid))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("userToEdit", "roleDTOs"))
                .andExpect(view().name("users/user-edit"));
    }

    @Test
    @WithMockUser(username = "test-admin", roles = {"USER", "ADMIN"})
    void testEditUserRedirectsWithErrorsOrToUsersListOnSuccess() throws Exception {
        User user = testData.createAndSaveUser();
        Set<Role> roles = user.getRoles();

        Role role = new Role();
        role.setImageUrl("test.png");
        role.setName("ADMIN");

        Role newRole = roleRepository.save(role);

        roles.add(newRole);

        user.setRoles(roles);

        UserDTO userDTO = mapper.map(user, UserDTO.class);

        TechStoreUserDetails techStoreUserDetails = new TechStoreUserDetails(user);

        mockMvc.perform(
                patch("/users/edit")
                        .flashAttr("userDTO", userDTO)
                        .with(csrf())
                        .with(user(techStoreUserDetails))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));

        userDTO = new UserDTO();

        mockMvc.perform(patch("/users/edit")
                        .flashAttr("userToEdit", userDTO)
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/users/edit/*"));
    }

    @Test
    @WithMockUser(username = "test-admin", roles = {"USER", "ADMIN"})
    void testDeleteUserCallsServiceWithUuid() throws Exception {
        User user = testData.createAndSaveUser();

        UUID uuid = user.getUuid();

        mockMvc.perform(
                    delete("/users/delete/{uuid}", uuid)
                    .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
    }

    @Test
    @WithMockUser(username = "test-admin", roles = {"USER", "ADMIN"})
    void testDeleteAllUsersCallsService() throws Exception {
        testData.createAndSaveUsers();

        mockMvc.perform(
                    delete("/users/delete-all")
                    .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
    }

    @Test
    @WithMockUser(username = "test-user")
    void testProfileAddsDataToModel() throws Exception {
        testData.createAndSaveUsers();

        mockMvc.perform(get("/users/profile"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("genders", "countries", "userProfileToEdit", "userCredentialsToEdit", "userPasswordToEdit"))
                .andExpect(view().name("users/user-profile"));
    }

    @Test
    @WithMockUser(username = "test-user")
    void testEditUserProfileRedirectsWithErrorsOrToProfilePageOnSuccess() throws Exception {
        testData.createAndSaveUser();

        UserProfileDTO userProfileDTO = createUserProfileDTO();

        mockMvc.perform(
                    patch("/users/profile/edit")
                    .flashAttr("userProfileDTO", userProfileDTO)
                    .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/profile"));

        userProfileDTO = new UserProfileDTO();
        userProfileDTO.setImage(createMultipartFile());
        userProfileDTO.setImageUrl("test.png");

        mockMvc.perform(
                    patch("/users/profile/edit")
                    .flashAttr("userProfileToEdit", userProfileDTO)
                    .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/profile"));
    }

    @Test
    void testEditUserCredentialsRedirectsWithErrorsOrToProfilePageOnSuccess() throws Exception {
        User user = createUser();

        TechStoreUserDetails techStoreUserDetails = new TechStoreUserDetails(user);

        UserCredentialsDTO userCredentialsDTO = new UserCredentialsDTO();
        userCredentialsDTO.setEmail("test@example.com");
        userCredentialsDTO.setUsername("test-user");

        mockMvc.perform(
                    patch("/users/credentials/edit")
                    .flashAttr("userCredentialsDTO", userCredentialsDTO)
                    .with(csrf())
                    .with(user(techStoreUserDetails))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/profile"));

        userCredentialsDTO = new UserCredentialsDTO();

        mockMvc.perform(
                    patch("/users/credentials/edit")
                    .flashAttr("userCredentialsToEdit", userCredentialsDTO)
                    .with(csrf())
                    .with(user(techStoreUserDetails))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/profile"));
    }

    @Test
    @WithMockUser(username = "test-user")
    void testEditUserPasswordRedirectsWithErrorsOrToProfilePageOnSuccess() throws Exception {
        testData.createAndSaveUser();

        UserPasswordDTO userPasswordDTO = new UserPasswordDTO();
        userPasswordDTO.setPassword("test-pass");
        userPasswordDTO.setNewPassword("test-new-pass");

        mockMvc.perform(
                    patch("/users/password/edit")
                    .flashAttr("userPasswordDTO", userPasswordDTO)
                    .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/profile"));

        mockMvc.perform(
                    patch("/users/password/edit")
                    .flashAttr("userPasswordToEdit", userPasswordDTO)
                    .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/profile"));
    }

    @Test
    @WithMockUser(username = "test-user")
    void testHandleEntityNotFoundError() throws Exception {
        testData.createAndSaveUser();

        UserPasswordDTO userPasswordDTO = new UserPasswordDTO();
        userPasswordDTO.setPassword("test-incorrect-pass");
        userPasswordDTO.setNewPassword("test-new-pass");

        mockMvc.perform(
                        patch("/users/password/edit")
                        .flashAttr("userPasswordDTO", userPasswordDTO)
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/profile"))
                .andExpect(flash().attribute("passwordError", INCORRECT_PASSWORD));
    }

    @Test
    void testHandleEmailFoundError() throws Exception {
        testData.createAndSaveUsers();

        UserCredentialsDTO userCredentialsDTO = new UserCredentialsDTO();
        userCredentialsDTO.setEmail("test2@example.com");
        userCredentialsDTO.setUsername("test-user");

        User user = userRepository.findByUsername("test-user").get();

        TechStoreUserDetails techStoreUserDetails = new TechStoreUserDetails(user);

        mockMvc.perform(
                        patch("/users/credentials/edit")
                        .with(csrf())
                        .flashAttr("userCredentialsDTO", userCredentialsDTO)
                        .with(user(techStoreUserDetails))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/profile"))
                .andExpect(flash().attribute("emailError", String.format(ENTITY_FOUND, "Email")));
    }

    @Test
    void testHandleUsernameFoundError() throws Exception {
        testData.createAndSaveUsers();

        UserCredentialsDTO userCredentialsDTO = new UserCredentialsDTO();
        userCredentialsDTO.setEmail("test@example.com");
        userCredentialsDTO.setUsername("test-user2");

        User user = userRepository.findByUsername("test-user").get();

        TechStoreUserDetails techStoreUserDetails = new TechStoreUserDetails(user);

        mockMvc.perform(
                        patch("/users/credentials/edit")
                        .with(csrf())
                        .flashAttr("userCredentialsDTO", userCredentialsDTO)
                        .with(user(techStoreUserDetails))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/profile"))
                .andExpect(flash().attribute("usernameError", String.format(ENTITY_FOUND, "User")));
    }

    private static UserProfileDTO createUserProfileDTO() throws IOException {
        UserProfileDTO userProfileDTO = new UserProfileDTO();

        userProfileDTO.setImage(createMultipartFile());
        userProfileDTO.setImageUrl("test.png");
        userProfileDTO.setFirstName("test-firstname");
        userProfileDTO.setLastName("test-lastname");
        userProfileDTO.setGender("Male");
        userProfileDTO.setPhoneNumber("0123456789");
        userProfileDTO.setAddress("test-address");
        userProfileDTO.setCountry("Bulgaria");
        userProfileDTO.setCity("test-city");
        userProfileDTO.setZipCode("test-zipcode");

        return userProfileDTO;
    }

}
