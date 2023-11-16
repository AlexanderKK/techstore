package com.techx7.techstore.service.impl;

import com.google.gson.Gson;
import com.techx7.techstore.model.dto.role.ImportRolesJsonDTO;
import com.techx7.techstore.model.entity.Role;
import com.techx7.techstore.repository.RoleRepository;
import com.techx7.techstore.repository.UserRepository;
import com.techx7.techstore.service.SeedService;
import com.techx7.techstore.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.techx7.techstore.constant.Messages.*;
import static com.techx7.techstore.constant.Paths.*;

@Service
public class SeedServiceImpl implements SeedService {

    private final Gson gson;
    private final ModelMapper mapper;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public SeedServiceImpl(Gson gson,
                           ModelMapper mapper,
                           RoleRepository roleRepository,
                           UserRepository userRepository,
                           UserService userService) {
        this.gson = gson;
        this.mapper = mapper;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    private static BufferedReader readFileFromResources(String fileName) {
        return new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(
                                ClassLoader.getSystemResourceAsStream(fileName))));
    }

    @Override
    public String seedRoles() {
        System.out.println("Importing user roles...");

        if(roleRepository.count() != 0) {
            return String.format(ENTITIES_ALREADY_SEEDED, "Roles");
        }

        BufferedReader bufferedReader = readFileFromResources(IMPORT_ROLES_JSON_PATH);

        ImportRolesJsonDTO[] roleJsonDTOs = gson.fromJson(bufferedReader, ImportRolesJsonDTO[].class);

        List<Role> roles = Arrays.stream(roleJsonDTOs)
                .map(roleJsonDTO -> mapper.map(roleJsonDTO, Role.class))
                .toList();

        roleRepository.saveAll(roles);

        return String.format(ENTITIES_SEEDED_SUCCESSFULLY, "Roles");
    }

    @Override
    public String seedAdmin() {
        System.out.println("Importing admin...");

        if(userRepository.count() != 0) {
            return String.format(ENTITIES_ALREADY_SEEDED, "Admin");
        }

        userService.createAdmin();

        return String.format(ENTITIES_SEEDED_SUCCESSFULLY, "Admin");
    }

}
