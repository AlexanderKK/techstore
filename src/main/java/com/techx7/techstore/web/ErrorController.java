package com.techx7.techstore.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorController {

    @GetMapping("/no-javascript")
    public String errorNoJavascript() {
        return "error/no-javascript";
    }

    @GetMapping("/forbidden")
    public String forbidden() {
        return "cart";
    }

}
