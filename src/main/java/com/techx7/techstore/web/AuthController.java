package com.techx7.techstore.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public String onFailure(@ModelAttribute("emailOrUsername") String emailOrUsername, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("bad_credentials", "true");
        redirectAttributes.addFlashAttribute("emailOrUsername", emailOrUsername);

        return "redirect:login";
    }

    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }

}
