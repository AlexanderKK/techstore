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
@RequestMapping("/users")
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
    public String getEditUser(Model model,
                              @PathVariable("uuid") UUID uuid) {
        UserDTO userDTO = userService.getUserByUuid(uuid);

        List<RoleDTO> roleDTOs = roleService.getAllRoles();

        model.addAttribute("roleDTOs", roleDTOs);
        model.addAttribute("userToEdit", userDTO);

        if(!model.containsAttribute("userDTO")) {
            model.addAttribute("userDTO", null);
        }

        return "user-edit";
    }

    @RequestMapping(value = "/edit", method = { RequestMethod.PATCH, RequestMethod.POST })
    public String editUser(@Valid UserDTO userDTO,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult", bindingResult);
            redirectAttributes.addFlashAttribute("userDTO", userDTO);

            return "redirect:/users/edit/" + userDTO.getUuid();
        }

        userService.editUser(userDTO);

        return "redirect:/admin";
    }

    @DeleteMapping("/delete/{uuid}")
    public String deleteUser(@PathVariable("uuid") UUID uuid) {
        userService.deleteUserByUuid(uuid);

        return "redirect:/admin";
    }

    @DeleteMapping("/delete-all")
    public String deleteAllUsers() {
        userService.deleteAllUsers();

        return "redirect:/admin";
    }

}
