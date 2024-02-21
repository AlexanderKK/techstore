package com.techx7.techstore.service;

import com.techx7.techstore.model.dto.review.AddReviewDTO;
import com.techx7.techstore.model.dto.review.ReviewDTO;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface ReviewService {

    void createReview(AddReviewDTO addReviewDTO, Principal principal);

    List<ReviewDTO> getAllReviewsByProductUuid(UUID productUuid, Principal principal);

}
