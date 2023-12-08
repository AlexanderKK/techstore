package com.techx7.techstore.web;

import com.techx7.techstore.exception.EmailFoundException;
import com.techx7.techstore.exception.UsernameFoundException;
import com.techx7.techstore.model.session.TechStoreUserDetails;
import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.model.dto.gender.GenderDTO;
import com.techx7.techstore.model.dto.country.CountryDTO;
import com.techx7.techstore.model.dto.role.RoleDTO;
import com.techx7.techstore.model.dto.user.UserCredentialsDTO;
import com.techx7.techstore.model.dto.user.UserDTO;
import com.techx7.techstore.model.dto.user.UserPasswordDTO;
import com.techx7.techstore.model.dto.user.UserProfileDTO;
import com.techx7.techstore.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
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
    private final MonitoringService monitoringService;

    @Autowired
    public UserController(UserService userService,
                          RoleService roleService,
                          GenderService genderService,
                          CountryService countryService,
                          MonitoringService monitoringService) {
        this.userService = userService;
        this.roleService = roleService;
        this.genderService = genderService;
        this.countryService = countryService;
        this.monitoringService = monitoringService;
    }

    @PreAuthorize("@hasAnyRole('ADMIN')")
    @GetMapping
    public String getUsers(Model model) {
        int usersLogins = monitoringService.getUsersLogins();

        model.addAttribute("usersLogins", usersLogins);

        List<UserDTO> users = userService.getAllUsers();

        model.addAttribute("users", users);

        return "users/users";
    }

    @PreAuthorize("@hasAnyRole('ADMIN')")
    @GetMapping("/edit/{uuid}")
    public String getUser(Model model,
                          @PathVariable("uuid") UUID uuid) {
        UserDTO userDTO = userService.getUserByUuid(uuid);

        List<RoleDTO> roleDTOs = roleService.getAllRoles();

        model.addAttribute("roleDTOs", roleDTOs);

        if(!model.containsAttribute("userToEdit")) {
            model.addAttribute("userToEdit", userDTO);
        }

        return "users/user-edit";
    }

    @PreAuthorize("@hasAnyRole('CARRIER', 'USER', 'SUPPORT', 'MANAGER', 'ADMIN')")
    @PatchMapping("/edit")
    public String editUser(@Valid UserDTO userDTO,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           @AuthenticationPrincipal TechStoreUserDetails loggedUser) {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userToEdit", userDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userToEdit", bindingResult);

            return "redirect:/users/edit/" + userDTO.getUuid();
        }

        userService.editUser(userDTO, loggedUser);

        return "redirect:/users";
    }

    @PreAuthorize("@hasAnyRole('ADMIN')")
    @DeleteMapping("/delete/{uuid}")
    public String deleteUser(@PathVariable("uuid") UUID uuid) {
        userService.deleteUserByUuid(uuid);

        return "redirect:/users";
    }

    @PreAuthorize("@hasAnyRole('ADMIN')")
    @DeleteMapping("/delete-all")
    public String deleteAllUsers() {
        userService.deleteAllUsers();

        return "redirect:/users";
    }

    @PreAuthorize("@hasAnyRole('CARRIER', 'USER', 'SUPPORT', 'MANAGER', 'ADMIN')")
    @GetMapping("/profile")
    public String profile(Model model,
                          Principal principal) {
        List<GenderDTO> genderDTOs = genderService.getAllGenders();
        List<CountryDTO> countryDTOs = countryService.getAllCountries();

        model.addAttribute("genders", genderDTOs);
        model.addAttribute("countries", countryDTOs);

        UserProfileDTO userProfileDTO = userService.getUserProfile(principal);
        UserCredentialsDTO userCredentialsDTO = userService.getUserCredentials(principal);
        UserPasswordDTO userPasswordDTO = userService.getUserPassword(principal);

        if(!model.containsAttribute("userProfileToEdit")) {
            model.addAttribute("userProfileToEdit", userProfileDTO);
        }

        if(!model.containsAttribute("userCredentialsToEdit")) {
            model.addAttribute("userCredentialsToEdit", userCredentialsDTO);
        }

        if(!model.containsAttribute("userPasswordToEdit")) {
            model.addAttribute("userPasswordToEdit", userPasswordDTO);
        }

        return "users/user-profile";
    }

    @PreAuthorize("@hasAnyRole('CARRIER', 'USER', 'SUPPORT', 'MANAGER', 'ADMIN')")
    @PatchMapping("/profile/edit")
    public String editUserProfile(@Valid UserProfileDTO userProfileDTO,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes,
                                  Principal principal) throws IOException {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userProfileToEdit", userProfileDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userProfileToEdit", bindingResult);

            return "redirect:/users/profile";
        }

        userService.editUserProfile(userProfileDTO, principal);

        return "redirect:/users/profile";
    }

    @PreAuthorize("@hasAnyRole('CARRIER', 'USER', 'SUPPORT', 'MANAGER', 'ADMIN')")
    @PatchMapping("/credentials/edit")
    public String editUserCredentials(@Valid UserCredentialsDTO userCredentialsDTO,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes,
                                  @AuthenticationPrincipal TechStoreUserDetails loggedUser) {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userCredentialsToEdit", userCredentialsDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userCredentialsToEdit", bindingResult);

            return "redirect:/users/profile";
        }

        userService.editUserCredentials(userCredentialsDTO, loggedUser);

        return "redirect:/users/profile";
    }

    @PreAuthorize("@hasAnyRole('CARRIER', 'USER', 'SUPPORT', 'MANAGER', 'ADMIN')")
    @PatchMapping("/password/edit")
    public String editUserPassword(@Valid UserPasswordDTO userPasswordDTO,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes,
                                      Principal principal) {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userPasswordToEdit", userPasswordDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userPasswordToEdit", bindingResult);

            return "redirect:/users/profile";
        }

        userService.editUserPassword(userPasswordDTO, principal);

        return "redirect:/users/profile";
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public String handleEntityNotFoundError(EntityNotFoundException ex,
                                       RedirectAttributes redirectAttributes) {
        System.out.println(ex.getMessage());

        redirectAttributes.addFlashAttribute("passwordError", ex.getMessage());

        return "redirect:/users/profile";
    }

    @ExceptionHandler(EmailFoundException.class)
    public String handleEmailFoundError(EmailFoundException ex,
                                        RedirectAttributes redirectAttributes) {
        System.out.println(ex.getMessage());

        redirectAttributes.addFlashAttribute("emailError", ex.getMessage());

        return "redirect:/users/profile";
    }

    @ExceptionHandler(UsernameFoundException.class)
    public String handleEmailFoundError(UsernameFoundException ex,
                                        RedirectAttributes redirectAttributes) {
        System.out.println(ex.getMessage());

        redirectAttributes.addFlashAttribute("usernameError", ex.getMessage());

        return "redirect:/users/profile";
    }

}
