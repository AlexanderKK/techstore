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
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/users")
//@CrossOrigin("http://localhost:8080")
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

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public Map<String, String> handleValidationExceptions(
//            MethodArgumentNotValidException ex) {
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach((error) -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            errors.put(fieldName, errorMessage);
//        });
//        return errors;
//    }

//    @ResponseBody
//    @PostMapping("/register")
//    public ResponseEntity<RegisterDTO> registerUser(@Valid @RequestBody RegisterDTO registerDTO) {
////        userService.register(registerDTO);
//
//        return ResponseEntity.ok(registerDTO);

//        if(bindingResult.hasErrors()) {
//            return new ResponseEntity<>(registerDTO, HttpStatus.OK);
////            return ResponseEntity.badRequest().body(bindingResult);
//        }

//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Location", "http://localhost:8080/users/login");

//        return ResponseEntity.ok().headers(headers).build();
//        return new ResponseEntity<BindingResult>(null, headers, HttpStatus.FOUND);
//        return ResponseEntity.created(
//                uriComponentsBuilder.path("/").build().toUri()
//        ).build();
//    }

}
