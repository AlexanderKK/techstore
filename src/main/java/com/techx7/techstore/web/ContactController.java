package com.techx7.techstore.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@CrossOrigin(origins = "*")
@Controller
public class ContactController {

    @GetMapping("/contact")
    public String maps() {
        return "contact";
    }

}
