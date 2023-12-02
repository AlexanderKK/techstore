package com.techx7.techstore.model.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    @ManyToOne
    private Model model;

    @NotNull(message = "Should not be empty")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "products_categories",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    @Valid
    private Set<Category> categories;

    @NotBlank(message = "Should not be empty")
    @Size(min = 5, max = 512)
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @NotNull(message = "Should not be empty")
    @DecimalMin(value = "1", message = "Price should be a positive number")
    @DecimalMax(value = "1000000", message = "Price limit is 1000000")
    @Column(nullable = false)
    private BigDecimal price;

    @DecimalMax(value = "1000000", message = "Discount price limit is 1000000")
    @Column(name = "discount_price")
    private BigDecimal discountPrice;

    @PositiveOrZero
    @NotNull(message = "Should not be empty")
    @Column(name = "initial_quantity", nullable = false)
    private Integer initialQuantity;

    @PositiveOrZero
    @NotNull(message = "Should not be empty")
    @Column(name = "available_quantity", nullable = false)
    private Integer availableQuantity;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotBlank(message = "Please add technical characteristics")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String specification;

    @NotNull(message = "Should not be empty")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime created = LocalDateTime.now();

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime modified;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private Set<CartItem> cartItems;

    public Product() {
        this.categories = new HashSet<>();
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getInitialQuantity() {
        return initialQuantity;
    }

    public void setInitialQuantity(Integer initialQuantity) {
        this.initialQuantity = initialQuantity;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }

}
