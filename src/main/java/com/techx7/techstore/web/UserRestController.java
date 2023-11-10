package com.techx7.techstore.web;

import com.techx7.techstore.model.dto.user.RegisterDTO;
import com.techx7.techstore.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterDTO> registerUser(@Valid @RequestBody RegisterDTO registerDTO) {
        userService.register(registerDTO);

        return ResponseEntity.ok().build();
    }

}
