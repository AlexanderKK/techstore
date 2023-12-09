package com.techx7.techstore.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    @ManyToOne
    private UserInfo userInfo;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "order_items",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "cart_item_id"))
    private List<CartItem> cartItems;

    private String status = "Pending";

    private String payment;

    @NotNull(message = "Creation date should not be empty")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime created = LocalDateTime.now();

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime modified;

    public Order() {}

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
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
