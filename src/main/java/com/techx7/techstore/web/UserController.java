package com.techx7.techstore.web;

import com.google.gson.Gson;
import com.techx7.techstore.model.dto.RegisterDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    private final Gson gson;

    @Autowired
    public UserController(Gson gson) {
        this.gson = gson;
    }

    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }

    @ResponseBody
    @PostMapping(value = "/register", consumes = "application/json")
    public String register(@RequestBody @Valid RegisterDTO registerDTO,
                                 BindingResult bindingResult) {
        if(!bindingResult.hasErrors()) {
            // TODO: Register User
        }

        return gson.toJson(bindingResult);
    }

}
