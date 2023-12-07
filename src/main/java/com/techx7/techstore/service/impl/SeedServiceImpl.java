package com.techx7.techstore.service.impl;

import com.google.gson.Gson;
import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.model.dto.role.ImportRolesJsonDTO;
import com.techx7.techstore.model.entity.Role;
import com.techx7.techstore.model.entity.User;
import com.techx7.techstore.repository.RoleRepository;
import com.techx7.techstore.repository.UserRepository;
import com.techx7.techstore.service.CloudinaryService;
import com.techx7.techstore.service.SeedService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.techx7.techstore.constant.Messages.*;
import static com.techx7.techstore.constant.Paths.IMPORT_ROLES_JSON_PATH;

@Service
public class SeedServiceImpl implements SeedService {

    private final Gson gson;
    private final ModelMapper mapper;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SeedServiceImpl(Gson gson,
                           ModelMapper mapper,
                           RoleRepository roleRepository,
                           UserRepository userRepository,
                           CloudinaryService cloudinaryService,
                           PasswordEncoder passwordEncoder) {
        this.gson = gson;
        this.mapper = mapper;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.cloudinaryService = cloudinaryService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String seedRoles() throws Exception {
        System.out.println("Importing user roles...");

        if(roleRepository.count() != 0) {
            return String.format(ENTITIES_ALREADY_SEEDED, "Roles");
        }

        BufferedReader bufferedReader = readFileFromResources(IMPORT_ROLES_JSON_PATH);

        ImportRolesJsonDTO[] roleJsonDTOs = gson.fromJson(bufferedReader, ImportRolesJsonDTO[].class);

        List<Role> roles = Arrays.stream(roleJsonDTOs)
                .map(roleJsonDTO -> mapper.map(roleJsonDTO, Role.class))
                .toList();

        for (Role role : roles) {
            String imageUrl = cloudinaryService.seedFile(
                    role.getClass().getSimpleName(), role.getName()
            );

            role.setImageUrl(imageUrl);
        }

        roleRepository.saveAll(roles);

        return String.format(ENTITIES_SEEDED_SUCCESSFULLY, "Roles");
    }

    @Override
    public String seedAdmin() {
        System.out.println("Importing admin...");

        if(userRepository.count() != 0) {
            return String.format(ENTITIES_ALREADY_SEEDED, "Admin");
        }

        createAdmin();

        return String.format(ENTITIES_SEEDED_SUCCESSFULLY, "Admin");
    }

    private void createAdmin() {
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

    private Role getRoleEntity(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "Role")));
    }

    private static BufferedReader readFileFromResources(String fileName) {
        return new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(
                                ClassLoader.getSystemResourceAsStream(fileName))));
    }

}
