package com.techx7.techstore.repository;

import com.techx7.techstore.model.entity.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = RoleRepository.class)
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.techx7.techstore.model.entity"})
@DataJpaTest
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    private UUID existingUuid;

    private String existingName;

    @BeforeEach
    void setUp() {
        roleRepository.deleteAll();

        Role existingRole = new Role();

        existingRole.setName("ADMIN");
        existingRole.setImageUrl("http://example.com/image.jpg");
        existingRole.setDescription("Administrator role with all privileges");
        existingUuid = UUID.randomUUID();
        existingName = existingRole.getName();

        roleRepository.save(existingRole);
    }

    @AfterEach
    void tearDown() {
        roleRepository.deleteAll();
    }

    @Test
    void testFindByNameWhenRoleExists() {
        Optional<Role> foundRole = roleRepository.findByName(existingName);

        assertThat(foundRole).isNotEmpty();
        assertThat(foundRole.get().getName()).isEqualTo(existingName);
    }

    @Test
    void testFindByNameWhenRoleDoesNotExist() {
        Optional<Role> foundRole = roleRepository.findByName("NON_EXISTENT_ROLE");

        assertThat(foundRole).isEmpty();
    }

    @Test
    void testDeleteByUuidWhenRoleExists() {
        roleRepository.deleteByUuid(existingUuid);
        Optional<Role> foundRole = roleRepository.findByUuid(existingUuid);

        assertThat(foundRole).isEmpty();
    }

    @Test
    void testDeleteByUuidWhenRoleDoesNotExist() {
        UUID nonExistentUuid = UUID.randomUUID();
        roleRepository.deleteByUuid(nonExistentUuid);
        long roleCount = roleRepository.count();

        assertThat(roleCount).isEqualTo(1); // Assuming there was only one role to begin with
    }

    @Test
    void testFindByUuidWhenRoleExists() {
        Optional<Role> existingRole = roleRepository.findByName("ADMIN");

        UUID existingUuid = existingRole.get().getUuid();

        Optional<Role> foundRole = roleRepository.findByUuid(existingUuid);

        List<Role> roles = roleRepository.findAll();

        assertThat(foundRole).isNotEmpty();
        assertThat(foundRole.get().getUuid()).isEqualTo(existingUuid);
    }

    @Test
    void testFindByUuidWhenRoleDoesNotExist() {
        UUID nonExistentUuid = UUID.randomUUID();
        Optional<Role> foundRole = roleRepository.findByUuid(nonExistentUuid);

        assertThat(foundRole).isEmpty();
    }

}
