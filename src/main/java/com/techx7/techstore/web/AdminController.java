package com.techx7.techstore.web;

import com.techx7.techstore.model.dto.user.UserDTO;
import com.techx7.techstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String index(Model model) {
        List<UserDTO> users = userService.getAllUsers();

        model.addAttribute("users", users);

        return "users-panel";
    }

}
