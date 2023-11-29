package com.techx7.techstore.web;

import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.exception.UserAlreadyActivatedException;
import com.techx7.techstore.exception.UserNotActivatedException;
import com.techx7.techstore.service.UserActivationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.techx7.techstore.constant.Messages.*;

@Controller
@RequestMapping("/users")
public class AuthController {

    private final UserActivationService userActivationService;

    @Autowired
    public AuthController(UserActivationService userActivationService) {
        this.userActivationService = userActivationService;
    }

    @GetMapping("/login")
    public String login(Model model) {
        if (!model.containsAttribute("bad_credentials")) {
            model.addAttribute("bad_credentials", null);
        }

        return "auth-login";
    }

    @PostMapping("/login-error")
    public String loginFailure(@ModelAttribute("emailOrUsername") String emailOrUsername, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("bad_credentials", "true");
        redirectAttributes.addFlashAttribute("emailOrUsername", emailOrUsername);

        return "redirect:login";
    }

    @GetMapping("/register/success")
    public String registerSuccess(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("verificationMessage", "Verification link has been sent to your email");

        return "redirect:/users/login";
    }

    @GetMapping("/activate")
    public String activateUserAccount(
            @RequestParam("activation_code") String activationCode,
            RedirectAttributes redirectAttributes) {
        String userName = userActivationService.activateUser(activationCode);

        redirectAttributes.addFlashAttribute("userActivated", USER_VERIFIED);

        return "redirect:/users/login";
    }

    @ExceptionHandler(UserNotActivatedException.class)
    public String handleUserNotActivatedError(RuntimeException ex) {
        System.out.println(ex.getMessage());

        return "redirect:/users/login";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
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
