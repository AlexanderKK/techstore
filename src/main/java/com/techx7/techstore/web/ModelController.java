package com.techx7.techstore.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/models")
public class ModelController {

    @GetMapping("/manage")
    public String manageModel() {
        return "model-management";
    }

}
