package com.techx7.techstore.service.impl;

import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.model.dto.role.AddRoleDTO;
import com.techx7.techstore.model.dto.role.RoleDTO;
import com.techx7.techstore.model.entity.Role;
import com.techx7.techstore.repository.RoleRepository;
import com.techx7.techstore.service.RoleService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.techx7.techstore.constant.Messages.ENTITY_NOT_FOUND;
import static com.techx7.techstore.util.FileUtils.saveFileLocally;

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
    public void createRole(AddRoleDTO addRoleDTO) throws IOException {
        saveFileLocally(addRoleDTO.getImage());

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

    @Override
    public RoleDTO getRoleByUuid(UUID uuid) {
        return roleRepository.findByUuid(uuid)
                .map(role -> mapper.map(role, RoleDTO.class))
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "Role")));
    }

    @Override
    public void editRole(RoleDTO roleDTO) throws IOException {
        Role role = roleRepository.findByUuid(roleDTO.getUuid())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "Role")));

        saveFileLocally(roleDTO.getImage());

        role.editRole(roleDTO);

        roleRepository.save(role);
    }

}
