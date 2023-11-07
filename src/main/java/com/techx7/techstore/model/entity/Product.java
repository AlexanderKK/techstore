package com.techx7.techstore.model.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    @NotNull(message = "Cannot be empty!")
    @ManyToOne(optional = false, cascade = CascadeType.REMOVE)
    private Model model;

    @NotNull(message = "Cannot be empty!")
    @ManyToMany(
            cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH},
            fetch = FetchType.EAGER)
    @JoinTable(name = "products_categories",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    @Valid
    private Set<Category> categories;

    @NotBlank(message = "Cannot be empty!")
    @Size(min = 5, max = 512)
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @NotNull(message = "Cannot be empty!")
    @DecimalMin(value = "1", message = "Price must be a positive number!")
    @DecimalMax(value = "1000000", message = "Price limit is 1000000!")
    @Column(nullable = false)
    private BigDecimal price;

    @DecimalMin(value = "1", message = "Discount must be at least 1%!")
    @DecimalMax(value = "100", message = "Discount limit is 100%!")
    @Column(name="discount_percentage")
    private BigDecimal discountPercentage;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotBlank(message = "You need to add technical characteristics!")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String specification;

    @NotNull(message = "Cannot be empty!")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private Calendar created = Calendar.getInstance();

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @Column(columnDefinition = "TIMESTAMP")
    private Calendar modified;

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

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
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

    public Calendar getCreated() {
        return created;
    }

    public void setCreated(Calendar created) {
        this.created = created;
    }

    public Calendar getModified() {
        return modified;
    }

    public void setModified(Calendar modified) {
        this.modified = modified;
    }

}
