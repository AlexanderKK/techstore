package com.techx7.techstore.testUtils;

import com.techx7.techstore.model.entity.Role;
import com.techx7.techstore.model.entity.User;
import com.techx7.techstore.repository.RoleRepository;
import com.techx7.techstore.repository.UserRepository;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class TestData {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    private void cleanAllTestData() {

    }

    public static User createUser() {
        User user = getUser();

        return user;
    }

    private static User getUser() {
        User user = new User();
        user.setEmail("mike@gmail.com");
        user.setUsername("mike");
        user.setPassword("mike1234");
        user.setRoles(Set.of(createRole()));
        user.setActive(true);

        return user;
    }

    public static Role createRole() {
        Role role = new Role();
        role.setName("TEST");
        role.setImageUrl("testRole.png");

        return role;
    }

    public User createAndSaveUser() {
        User user = getUser();

        roleRepository.saveAll(user.getRoles());

        return userRepository.save(user);
    }

}
