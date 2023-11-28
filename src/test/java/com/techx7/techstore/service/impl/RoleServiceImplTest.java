package com.techx7.techstore.service.impl;

import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.model.dto.role.AddRoleDTO;
import com.techx7.techstore.model.dto.role.RoleDTO;
import com.techx7.techstore.model.entity.Role;
import com.techx7.techstore.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.techx7.techstore.testUtils.TestData.createMultipartFile;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    void testGetAllRoles() {
        List<Role> roleList = new ArrayList<>();
        roleList.add(new Role());
        roleList.add(new Role());

        when(roleRepository.findAll()).thenReturn(roleList);
        when(modelMapper.map(any(Role.class), eq(RoleDTO.class))).thenReturn(new RoleDTO());

        List<RoleDTO> roleDTOList = roleService.getAllRoles();

        assertNotNull(roleDTOList);
        assertEquals(roleList.size(), roleDTOList.size());

        verify(roleRepository, times(1)).findAll();
        verify(modelMapper, times(roleList.size())).map(any(Role.class), eq(RoleDTO.class));
    }

    @Test
    void testCreateRole() {
        AddRoleDTO addRoleDTO = new AddRoleDTO();
        addRoleDTO.setName("Test Role");
        addRoleDTO.setImage(createMultipartFile());

        Role role = new Role();
        role.setName(addRoleDTO.getName());

        when(modelMapper.map(addRoleDTO, Role.class)).thenReturn(role);
        when(roleRepository.save(role)).thenReturn(role);

        assertDoesNotThrow(() -> roleService.createRole(addRoleDTO));

        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void testDeleteAllRoles() {
        doNothing().when(roleRepository).deleteAll();

        assertDoesNotThrow(() -> roleService.deleteAllRoles());

        verify(roleRepository, times(1)).deleteAll();
    }

    @Test
    void testDeleteRoleByUuid() {
        UUID uuid = UUID.randomUUID();

        doNothing().when(roleRepository).deleteByUuid(uuid);

        assertDoesNotThrow(() -> roleService.deleteRoleByUuid(uuid));

        verify(roleRepository, times(1)).deleteByUuid(uuid);
    }

    @Test
    void testGetRoleByUuid() {
        UUID uuid = UUID.randomUUID();
        Role role = new Role();
        role.setUuid(uuid);
        role.setImageUrl("test.png");

        when(roleRepository.findByUuid(uuid)).thenReturn(Optional.of(role));

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setUuid(role.getUuid());

        when(modelMapper.map(role, RoleDTO.class)).thenReturn(roleDTO);

        roleDTO = roleService.getRoleByUuid(uuid);

        assertNotNull(roleDTO);
        assertEquals(uuid, roleDTO.getUuid());

        verify(roleRepository, times(1)).findByUuid(uuid);
        verify(modelMapper, times(1)).map(role, RoleDTO.class);
    }

    @Test
    void testGetRoleByUuidThrowsEntityNotFoundException() {
        UUID uuid = UUID.randomUUID();

        when(roleRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> roleService.getRoleByUuid(uuid));

        verify(roleRepository, times(1)).findByUuid(uuid);
    }

    @Test
    void testEditRole() throws IOException {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setUuid(UUID.randomUUID());
        roleDTO.setName("testName");
        roleDTO.setImage(createMultipartFile());

        Role role = new Role();
        role.setUuid(roleDTO.getUuid());
        role.setImageUrl("test.png");
        role.setName("testName");

        when(roleRepository.findByUuid(roleDTO.getUuid())).thenReturn(Optional.of(role));

        when(modelMapper.map(roleDTO, Role.class)).thenReturn(role);

        when(roleRepository.save(role)).thenReturn(role);

        assertDoesNotThrow(() -> roleService.editRole(roleDTO));

        verify(roleRepository, times(1)).findByUuid(roleDTO.getUuid());
        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void testEditRoleThrowsEntityNotFoundException() {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setUuid(UUID.randomUUID());

        when(roleRepository.findByUuid(roleDTO.getUuid())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> roleService.editRole(roleDTO));

        verify(roleRepository, times(1)).findByUuid(roleDTO.getUuid());
        verify(modelMapper, never()).map(any(RoleDTO.class), eq(Role.class));
        verify(roleRepository, never()).save(any(Role.class));
    }

}
