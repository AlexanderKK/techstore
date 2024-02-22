package com.techx7.techstore.model.dto.review;

public class ReviewsDataForProductDTO {

    private int reviews;
    private Double averageRating;

    public ReviewsDataForProductDTO(int reviews, Double averageRating) {
        this.reviews = reviews;
        this.averageRating = averageRating;
    }

    public int getReviews() {
        return reviews;
    }

    public void setReviews(int reviews) {
        this.reviews = reviews;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

}
