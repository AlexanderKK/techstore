package com.techx7.techstore.web;

import com.techx7.techstore.exception.ReviewAlreadyExistingException;
import com.techx7.techstore.model.dto.review.AddReviewDTO;
import com.techx7.techstore.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    private static final String BINDING_RESULT_PATH = "org.springframework.validation.BindingResult";
    private static final String DOT = ".";
    private static final String addFlashAttribute = "addReviewDTO";
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PreAuthorize("@hasRole('USER')")
    @PostMapping("/add")
    public String createReview(@Valid AddReviewDTO addReviewDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               Principal principal) {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(addFlashAttribute, addReviewDTO);
            redirectAttributes.addFlashAttribute(
                    BINDING_RESULT_PATH + DOT + addFlashAttribute, bindingResult);

            return "redirect:/products/detail/" + addReviewDTO.getProduct();
        }

        reviewService.createReview(addReviewDTO, principal);

        return "redirect:/products/detail/" + addReviewDTO.getProduct();
    }

    @ExceptionHandler(ReviewAlreadyExistingException.class)
    public String handleExistingReviewError(ReviewAlreadyExistingException e,
                                            RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("existingReviewError", e.getMessage());

        return "redirect:/products/detail/" + e.getProductUuid();
    }

}
