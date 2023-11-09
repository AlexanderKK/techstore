package com.techx7.techstore.web;

import com.techx7.techstore.model.dto.user.RegisterDTO;
import com.techx7.techstore.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("http://localhost:8080/users/register")
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users/register")
    public ResponseEntity<RegisterDTO> registerUser(@Valid @RequestBody RegisterDTO registerDTO) {
        userService.register(registerDTO);

        return ResponseEntity.ok().build();
    }

}
