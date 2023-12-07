package com.techx7.techstore.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@CrossOrigin(origins = {"http://localhost:8080", "https://techx7.yellowflower-41c8e8d4.westeurope.azurecontainerapps.io"})
@Controller
public class ContactController {

    @GetMapping("/contact")
    public String maps() {
        return "contact";
    }

}
