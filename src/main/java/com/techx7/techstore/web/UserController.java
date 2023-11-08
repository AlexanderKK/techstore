package com.techx7.techstore.web;

import com.google.gson.Gson;
import com.techx7.techstore.model.dto.user.RegisterDTO;
import com.techx7.techstore.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@RequestMapping("/users")
@CrossOrigin("http://localhost:8080")
public class UserController {

    private final Gson gson;
    private final UserService userService;

    @Autowired
    public UserController(Gson gson,
                          UserService userService) {
        this.gson = gson;
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }

//    @ResponseBody
//    @PostMapping(value = "/register", consumes = "application/json")
//    public String register(@RequestBody @Valid RegisterDTO registerDTO,
//                                 BindingResult bindingResult) {
//        if(!bindingResult.hasErrors()) {
//            // TODO: Register User
//        }
//
//        return gson.toJson(bindingResult);
//    }

    @ResponseBody
    @PostMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<BindingResult> registerUser(@Valid @RequestBody RegisterDTO registerDTO,
                                                    UriComponentsBuilder uriComponentsBuilder,
                                                    BindingResult bindingResult) {
//        userService.register(registerDTO);

        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult);
        }

//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Location", "http://localhost:8080/users/login");

//        return ResponseEntity.ok().headers(headers).build();
//        return new ResponseEntity<BindingResult>(null, headers, HttpStatus.FOUND);
        return ResponseEntity.created(
                uriComponentsBuilder.path("/").build().toUri()
        ).build();
    }

}
