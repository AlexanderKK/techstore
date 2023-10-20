package com.techx7.techstore.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ContactController {

    @CrossOrigin(origins = "https://maps.googleapis.com/maps")
    @GetMapping("/contact")
    public String maps() {
        return "contact";
    }

}
