package com.techx7.techstore.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;
import java.util.Calendar;

@Entity
@Table(name = "coupons")
public class Coupon extends BaseEntity {

    @NotNull(message = "Cannot be empty!")
    @DecimalMin(value = "1", message = "Price must be a positive number!")
    @DecimalMax(value = "1000000", message = "Price limit is 1000000!")
    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "####.##")
    private BigDecimal amount;

    @NotNull(message = "Cannot be empty!")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @Column(name="date_sold", nullable = false, columnDefinition = "TIMESTAMP")
    private Calendar dateSold;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @Column(name="date_redeemed", columnDefinition = "TIMESTAMP")
    private Calendar dateRedeemed;

    @NotNull(message = "Cannot be empty!")
    @ManyToOne(optional = false)
    private User user;

    @ManyToOne
    private Order order;

    public Coupon() {}

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Calendar getDateSold() {
        return dateSold;
    }

    public void setDateSold(Calendar dateSold) {
        this.dateSold = dateSold;
    }

    public Calendar getDateRedeemed() {
        return dateRedeemed;
    }

    public void setDateRedeemed(Calendar dateRedeemed) {
        this.dateRedeemed = dateRedeemed;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

}
