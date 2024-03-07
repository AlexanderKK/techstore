package com.techx7.techstore.service.impl;

import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.exception.ReviewAlreadyExistingException;
import com.techx7.techstore.model.dto.review.AddReviewDTO;
import com.techx7.techstore.model.dto.review.ReviewDTO;
import com.techx7.techstore.model.entity.Product;
import com.techx7.techstore.model.entity.Review;
import com.techx7.techstore.model.entity.User;
import com.techx7.techstore.model.entity.UserInfo;
import com.techx7.techstore.repository.ProductRepository;
import com.techx7.techstore.repository.ReviewRepository;
import com.techx7.techstore.repository.UserRepository;
import com.techx7.techstore.service.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.techx7.techstore.constant.Messages.ENTITY_NOT_FOUND;
import static com.techx7.techstore.constant.Messages.REVIEW_ALREADY_EXISTING;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ModelMapper mapper;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             UserRepository userRepository,
                             ProductRepository productRepository,
                             ModelMapper mapper) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.productRepository = productRepository;
    }

    @Override
    public void createReview(AddReviewDTO addReviewDTO, Principal principal) {
        User user = getUserOrThrow(principal);

        Product product = getProductByUuidOrThrow(addReviewDTO.getProduct());

        Optional<Review> existingReview = reviewRepository.findByUserAndProduct(user, product);
        if(existingReview.isPresent()) {
            throw new ReviewAlreadyExistingException(REVIEW_ALREADY_EXISTING);
        }

        Review review = mapper.map(addReviewDTO, Review.class);

        review.setUser(user);
        review.setProduct(product);

        reviewRepository.save(review);
    }

    @Override
    public List<ReviewDTO> getAllReviewsByProductUuid(UUID productUuid, Principal principal) {
        Product product = getProductByUuidOrThrow(productUuid);

        List<Review> reviews = reviewRepository
                .findAllByProduct(product)
                .stream()
                .sorted((f, s) -> s.getCreated().compareTo(f.getCreated()))
                .toList();

        List<ReviewDTO> reviewDTOs = reviews.stream()
                .map(review -> {
                    ReviewDTO reviewDTO = mapper.map(review, ReviewDTO.class);

                    UserInfo userInfo = review.getUser().getUserInfo();
                    if(userInfo != null) {
                        String imageUrl = userInfo.getImageUrl();
                        reviewDTO.setImageUrl(imageUrl);
                    }

                    return reviewDTO;
                })
                .collect(Collectors.toList());

        return reviewDTOs;
    }

    private User getUserOrThrow(Principal principal) {
        return userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "User")));
    }

    private Product getProductByUuidOrThrow(UUID uuid) {
        return productRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "Product")));
    }

}
