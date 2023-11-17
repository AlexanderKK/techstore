package com.techx7.techstore.service.impl;

import com.techx7.techstore.model.dto.role.AddRoleDTO;
import com.techx7.techstore.model.dto.role.RoleDTO;
import com.techx7.techstore.model.entity.Category;
import com.techx7.techstore.model.entity.Role;
import com.techx7.techstore.repository.RoleRepository;
import com.techx7.techstore.service.RoleService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final ModelMapper mapper;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository,
                           ModelMapper mapper) {
        this.roleRepository = roleRepository;
        this.mapper = mapper;
    }

    @Override
    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(category -> mapper.map(category, RoleDTO.class))
                .toList();
    }

    @Override
    public void createRole(AddRoleDTO addRoleDTO) {
        Role role = mapper.map(addRoleDTO, Role.class);

        roleRepository.save(role);
    }

    @Override
    public void deleteAllRoles() {
        roleRepository.deleteAll();
    }

    @Override
    @Transactional
    public void deleteRoleByUuid(UUID uuid) {
        roleRepository.deleteByUuid(uuid);
    }

}
