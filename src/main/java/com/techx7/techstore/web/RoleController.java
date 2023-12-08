package com.techx7.techstore.web;

import com.techx7.techstore.model.dto.role.AddRoleDTO;
import com.techx7.techstore.model.dto.role.RoleDTO;
import com.techx7.techstore.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.techx7.techstore.constant.Paths.BINDING_RESULT_PATH;
import static com.techx7.techstore.constant.Paths.DOT;

@Controller
@RequestMapping("/roles")
public class RoleController {

    private final static String flashAttributeDTO = "addRoleDTO";
    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public String getRoles(Model model) {
        List<RoleDTO> roleDTOs = roleService.getAllRoles();

        model.addAttribute("roles", roleDTOs);

        if(!model.containsAttribute(flashAttributeDTO)) {
            model.addAttribute(flashAttributeDTO, new AddRoleDTO());
        }

        return "roles/roles";
    }

    @PostMapping("/add")
    public String addRole(@Valid AddRoleDTO addRoleDTO,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) throws IOException {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(flashAttributeDTO, addRoleDTO);
            redirectAttributes.addFlashAttribute(BINDING_RESULT_PATH + DOT + flashAttributeDTO, bindingResult);

            return "redirect:/roles";
        }

        roleService.createRole(addRoleDTO);

        return "redirect:/roles";
    }

    @GetMapping("/edit/{uuid}")
    public String getRole(Model model,
                              @PathVariable("uuid") UUID uuid) {
        RoleDTO roleDTO = roleService .getRoleByUuid(uuid);

        if(!model.containsAttribute("roleToEdit")) {
            model.addAttribute("roleToEdit", roleDTO);
        }

        return "roles/role-edit";
    }

    @PatchMapping("/edit")
    public String editUser(@Valid RoleDTO roleDTO,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) throws IOException {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("roleToEdit", roleDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.roleToEdit", bindingResult);

            return "redirect:/roles/edit/" + roleDTO.getUuid();
        }

        roleService.editRole(roleDTO);

        return "redirect:/roles";
    }

    @DeleteMapping("/delete/{uuid}")
    public String deleteRole(@PathVariable("uuid") UUID uuid) {
        roleService.deleteRoleByUuid(uuid);

        return "redirect:/roles";
    }

}
