package com.techx7.techstore.web;

import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.exception.UserAlreadyActivatedException;
import com.techx7.techstore.exception.UserNotActivatedException;
import com.techx7.techstore.service.UserActivationService;
import com.techx7.techstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.techx7.techstore.constant.Messages.*;

@Controller
@RequestMapping("/users")
public class AuthController {

    private final UserActivationService userActivationService;
    private final UserService userService;

    @Autowired
    public AuthController(UserActivationService userActivationService,
                          UserService userService) {
        this.userActivationService = userActivationService;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(Model model) {
        if (!model.containsAttribute("badCredentials")) {
            model.addAttribute("badCredentials", null);
        }

        return "login";
    }

    @PostMapping("/login-error")
    public String loginFailure(@ModelAttribute("emailOrUsername") String emailOrUsername, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("emailOrUsername", emailOrUsername);

        boolean isUserNonActive = userService.isUserNonActive(emailOrUsername);
        if(isUserNonActive) {
            redirectAttributes.addFlashAttribute("accountNotVerified", ACCOUNT_NOT_VERIFIED);
        } else {
            redirectAttributes.addFlashAttribute("badCredentials", "true");
        }

        return "redirect:login";
    }

    @GetMapping("/register/success")
    public String registerSuccess(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("verificationMessage", VERIFICATION_LINK_SENT);

        return "redirect:/users/login";
    }

    @GetMapping("/activate")
    public String activateUserAccount(
            @RequestParam("activation_code") String activationCode,
            RedirectAttributes redirectAttributes) {
        userActivationService.activateUser(activationCode);

        redirectAttributes.addFlashAttribute("userActivated", USER_VERIFIED);

        return "redirect:/users/login";
    }

    @ExceptionHandler(UserNotActivatedException.class)
    public String handleUserNotActivatedError(RuntimeException ex) {
        System.out.println(ex.getMessage());

        return "redirect:/users/login";
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public String handleActivationCodeNotFoundError(EntityNotFoundException ex,
                                  RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("activationCodeErrorMessage", ex.getMessage());

        return "redirect:/";
    }

    @ExceptionHandler(UserAlreadyActivatedException.class)
    public String handleUserAlreadyActivatedError(UserAlreadyActivatedException ex,
                                  RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("userAlreadyActivated", ex.getMessage());

        return "redirect:/";
    }

}
