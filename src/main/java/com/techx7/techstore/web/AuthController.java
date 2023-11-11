package com.techx7.techstore.web;

import com.techx7.techstore.exception.ForbiddenException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class AuthController {

    @GetMapping("/login")
    public String login(Model model) {
        if (!model.containsAttribute("bad_credentials")) {
            model.addAttribute("bad_credentials", null);
        }

        return "auth-login";
    }

    @PostMapping("/login-error")
    public String onFailure(@ModelAttribute("username") String username, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("bad_credentials", "true");
        redirectAttributes.addFlashAttribute("username", username);

        return "redirect:login";
    }

    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }

}
