package com.techx7.techstore.web.rest;

import com.techx7.techstore.exception.ReviewAlreadyExistingException;
import com.techx7.techstore.model.api.ApiError;
import com.techx7.techstore.model.dto.review.AddReviewDTO;
import com.techx7.techstore.model.dto.review.ReviewDTO;
import com.techx7.techstore.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reviews")
public class ReviewRestController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewRestController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/product/{uuid}")
    public ResponseEntity<List<ReviewDTO>> getReviews(@PathVariable("uuid") UUID productUuid,
                                                      Principal principal) {
        List<ReviewDTO> reviewDTOs = reviewService.getAllReviewsByProductUuid(productUuid, principal);

        return ResponseEntity.ok().body(reviewDTOs);
    }

    @PreAuthorize("@hasRole('USER')")
    @PostMapping("/add")
    public ResponseEntity<AddReviewDTO> createReview(@Valid @RequestBody AddReviewDTO addReviewDTO,
                                                     Principal principal) {
        reviewService.createReview(addReviewDTO, principal);

        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(ReviewAlreadyExistingException.class)
    public ResponseEntity<Object> handleReviewAlreadyExisting(ReviewAlreadyExistingException e) {
        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, e.getMessage());

        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}
