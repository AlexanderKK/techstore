package com.techx7.techstore.testUtils;

import com.techx7.techstore.model.entity.Role;
import com.techx7.techstore.model.entity.User;
import com.techx7.techstore.repository.RoleRepository;
import com.techx7.techstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class TestData {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    public void cleanAllTestData() {
        roleRepository.deleteAll();
        userRepository.deleteAll();
    }

    public static User createUser() {
        User user = getUser();

        return user;
    }

    private static User getUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setUsername("test-user");
        user.setPassword("test-pass");
        user.setRoles(Set.of(createRole()));
        user.setActive(true);

        return user;
    }

    public static Role createRole() {
        Role role = new Role();
        role.setName("USER");
        role.setImageUrl("testRole.png");

        return role;
    }

    public User createAndSaveUser() {
        User user = getUser();

        Set<Role> roles = new HashSet<>(roleRepository.saveAll(user.getRoles()));

        user.setRoles(roles);

        return userRepository.save(user);
    }

    public static MockMultipartFile createMultipartFile() {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "test.png",
                MediaType.IMAGE_PNG_VALUE,
                "TestImage".getBytes()
        );

        return file;
    }

}
