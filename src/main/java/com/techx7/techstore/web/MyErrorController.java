package com.techx7.techstore.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class MyErrorController implements ErrorController {

    @GetMapping
    public String renderErrorPage(HttpServletRequest request, Model model) {
        int statusCode = getErrorCode(request);

//        if(statusCode == HttpStatus.NOT_FOUND.value()) {
//            return "error/404";
//        }
//        else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
//            return "error/500";
//        }

        return switch (statusCode) {
//            case 400 -> "error/400";
//            case 401 -> "error/401";
            case 403 -> {
                model.addAttribute("errorMsg", "Http Error Code: 403. Unauthorized");
                yield "error/403";
            }
            case 404 -> "error/404";
            case 500 -> "error/500";
            default -> "error/generic";
        };
    }

    @GetMapping("/no-javascript")
    public String renderNoJavascript() {
        return "error/no-javascript";
    }

    private int getErrorCode(HttpServletRequest httpRequest) {
        return (Integer) httpRequest
                .getAttribute("jakarta.servlet.error.status_code");
    }

}
