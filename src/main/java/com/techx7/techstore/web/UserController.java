package com.techx7.techstore.web;

import com.techx7.techstore.model.dto.GenderDTO;
import com.techx7.techstore.model.dto.country.CountryDTO;
import com.techx7.techstore.model.dto.role.RoleDTO;
import com.techx7.techstore.model.dto.user.UserDTO;
import com.techx7.techstore.model.dto.user.UserProfileDTO;
import com.techx7.techstore.service.CountryService;
import com.techx7.techstore.service.GenderService;
import com.techx7.techstore.service.RoleService;
import com.techx7.techstore.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final GenderService genderService;
    private final CountryService countryService;

    @Autowired
    public UserController(UserService userService,
                          RoleService roleService,
                          GenderService genderService,
                          CountryService countryService) {
        this.userService = userService;
        this.roleService = roleService;
        this.genderService = genderService;
        this.countryService = countryService;
    }

    @GetMapping
    public String getUsers(Model model) {
        List<UserDTO> users = userService.getAllUsers();

        model.addAttribute("users", users);

        return "users";
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

            return "redirect:/users/edit/" + userDTO.getUuid();
        }

        userService.editUser(userDTO);

        return "redirect:/users";
    }

    @DeleteMapping("/delete/{uuid}")
    public String deleteUser(@PathVariable("uuid") UUID uuid) {
        userService.deleteUserByUuid(uuid);

        return "redirect:/users";
    }

    @DeleteMapping("/delete-all")
    public String deleteAllUsers() {
        userService.deleteAllUsers();

        return "redirect:/users";
    }

    @GetMapping("/profile")
    public String profile(Model model,
                          Principal principal) {
        List<GenderDTO> genderDTOs = genderService.getAllGenders();
        List<CountryDTO> countryDTOs = countryService.getAllCountries();

        model.addAttribute("genders", genderDTOs);
        model.addAttribute("countries", countryDTOs);

        UserDTO userDTO = userService.getUser(principal);

        UserProfileDTO userProfileDTO = userService.getUserProfile(principal);

//        model.addAttribute("userDTO", userDTO);

//        model.addAttribute("userProfileDTO", userProfileDTO);

        if(!model.containsAttribute("userProfileToEdit")) {
            model.addAttribute("userProfileToEdit", userProfileDTO);
        }

        return "user-profile";
    }

    @PatchMapping("/profile/edit")
    public String editUserProfile(@Valid UserProfileDTO userProfileDTO,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes,
                                  Principal principal) {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userProfileToEdit", userProfileDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userProfileToEdit", bindingResult);

            return "redirect:/users/profile";
        }

        userService.editUserProfile(userProfileDTO, principal);

        return "redirect:/users/profile";
    }

}
