package com.techx7.techstore.model.dto.review;

import com.techx7.techstore.validation.user.ExistingEmail;
import jakarta.validation.constraints.*;

import java.util.UUID;

public class AddReviewDTO {

    @NotNull(message = "Please pick a rating")
    @Min(value = 1, message = "Please choose a minimum rating of 1")
    @Max(value = 5, message = "Please choose a maximum rating of 5")
    private Double rating;

    @Size(max = 255, message = "Please do not exceed the maximum length of 255 characters")
    private String content;

    @NotNull(message = "Please enter a name")
    @Size(min = 3, max = 25, message = "Name should have from 3 to 25 characters")
    private String name;

    @ExistingEmail
    @NotBlank(message = "Please enter your email")
    private String email;

    @NotNull(message = "Product id should not be empty")
    private UUID product;

    public AddReviewDTO() {}

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UUID getProduct() {
        return product;
    }

    public void setProduct(UUID product) {
        this.product = product;
    }

}
