package com.techx7.techstore.service;

import com.techx7.techstore.model.dto.role.AddRoleDTO;
import com.techx7.techstore.model.dto.role.RoleDTO;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface RoleService {

    List<RoleDTO> getAllRoles();

    void createRole(AddRoleDTO addRoleDTO);

    void deleteAllRoles();

    void deleteRoleByUuid(UUID uuid);

    RoleDTO getRoleByUuid(UUID uuid);

    void editRole(RoleDTO roleDTO) throws IOException;

}
