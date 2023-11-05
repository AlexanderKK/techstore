package com.techx7.techstore.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Controller
@ControllerAdvice
public class GlobalErrorHandler {

    //https://jira.spring.io/browse/SPR-14651
    //Spring 4.3.5 supports RedirectAttributes
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
////    @ExceptionHandler(MultipartException.class)
////    public String handleError1(MultipartException e, RedirectAttributes redirectAttributes) {
////
////        redirectAttributes.addFlashAttribute("message", e.getCause().getMessage());
////        return "redirect:/uploadStatus";
////    }

//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler(MaxUploadSizeExceededException.class)
//    public String handleError2(MaxUploadSizeExceededException e, RedirectAttributes redirectAttributes) {
//        System.out.println("Error caught");
//
//        redirectAttributes.addFlashAttribute("message", e.getCause().getMessage());
//        return "redirect:/";
//
//    }

    /* Spring < 4.3.5
	@ExceptionHandler(MultipartException.class)
    public String handleError2(MultipartException e) {

        return "redirect:/errorPage";

    }*/

}
