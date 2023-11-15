package com.techx7.techstore.web.rest;

import com.techx7.techstore.model.dto.user.RegisterDTO;
import com.techx7.techstore.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("/users")
public class AuthRestController {

    private final UserService userService;

    @Autowired
    public AuthRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterDTO> registerUser(@Valid @RequestBody RegisterDTO registerDTO) {
        userService.register(registerDTO);

        return ResponseEntity.ok().build();
    }

}
