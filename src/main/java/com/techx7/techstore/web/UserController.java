package com.techx7.techstore.web;

import com.techx7.techstore.model.dto.role.RoleDTO;
import com.techx7.techstore.model.dto.user.UserDTO;
import com.techx7.techstore.service.RoleService;
import com.techx7.techstore.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin/users")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserController(UserService userService,
                          RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/edit/{uuid}")
    public String getUser(Model model,
                              @PathVariable("uuid") UUID uuid) {
        UserDTO userDTO = userService.getUserByUuid(uuid);

        List<RoleDTO> roleDTOs = roleService.getAllRoles();

        model.addAttribute("roleDTOs", roleDTOs);

        if(!model.containsAttribute("userToEdit")) {
            model.addAttribute("userToEdit", userDTO);
        }

        return "user-edit";
    }

    @PatchMapping("/edit")
    public String editUser(@Valid UserDTO userDTO,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userToEdit", userDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userToEdit", bindingResult);

            return "redirect:/admin/users/edit/" + userDTO.getUuid();
        }

        userService.editUser(userDTO);

        return "redirect:/admin/users";
    }

    @DeleteMapping("/delete/{uuid}")
    public String deleteUser(@PathVariable("uuid") UUID uuid) {
        userService.deleteUserByUuid(uuid);

        return "redirect:/admin/users";
    }

    @DeleteMapping("/delete-all")
    public String deleteAllUsers() {
        userService.deleteAllUsers();

        return "redirect:/admin/users";
    }

}
