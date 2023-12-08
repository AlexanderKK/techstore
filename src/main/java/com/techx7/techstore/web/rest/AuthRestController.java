package com.techx7.techstore.web.rest;

import com.techx7.techstore.model.dto.user.RegisterDTO;
import com.techx7.techstore.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:8080", "https://techx7.yellowflower-41c8e8d4.westeurope.azurecontainerapps.io"})
@RestController
@RequestMapping("/users")
public class AuthRestController {

    private final UserService userService;

    @Autowired
    public AuthRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterDTO> registerUser(@Valid @RequestBody RegisterDTO registerDTO, HttpServletRequest httpServletRequest) {
        String remoteAddr = httpServletRequest.getRemoteAddr();
        registerDTO.setIpAddress(remoteAddr);

        userService.register(registerDTO);

        System.out.println("main");

        return ResponseEntity.ok().build();
    }

}
