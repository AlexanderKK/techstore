package com.techx7.techstore.web;

import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.exception.UserAlreadyActivatedException;
import com.techx7.techstore.exception.UserNotActivatedException;
import com.techx7.techstore.service.EmailService;
import com.techx7.techstore.service.UserActivationService;
import com.techx7.techstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.techx7.techstore.constant.Messages.USER_VERIFIED;

@Controller
@RequestMapping("/users")
public class AuthController {

    private final UserActivationService userActivationService;
    private final UserService userService;
    private final EmailService emailService;

    @Autowired
    public AuthController(UserActivationService userActivationService,
                          UserService userService,
                          EmailService emailService) {
        this.userActivationService = userActivationService;
        this.userService = userService;
        this.emailService = emailService;
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
        redirectAttributes.addFlashAttribute("emailOrUsername", emailOrUsername);

        boolean isUserNonActive = userService.isUserNonActive(emailOrUsername);
        if(isUserNonActive) {
            redirectAttributes.addFlashAttribute("accountNotVerified", "Account not verified. Check your email for verification code");
        } else {
            redirectAttributes.addFlashAttribute("bad_credentials", "true");
        }

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

    @GetMapping("/forgottenpassword")
    public String forgottenPassword() {
        return "forgotten-password";
    }

    @PostMapping("/forgottenpassword/sendmail")
    public String forgottenPasswordSendMail(@RequestParam String emailOrUsername,
                                            RedirectAttributes redirectAttributes) {
        if(!userService.isUserPresent(emailOrUsername)) {
            redirectAttributes.addFlashAttribute("emailOrUsername", emailOrUsername);
            redirectAttributes.addFlashAttribute("bad_credentials", true);

            return "redirect:/users/forgottenpassword";
        }

        redirectAttributes.addFlashAttribute("verificationMessage", "Password recovery link has been sent to your email");

        emailService.sendPasswordRecoveryEmail(emailOrUsername);

        return "redirect:/users/forgottenpassword";
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
