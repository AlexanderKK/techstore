package com.techx7.techstore.model.dto.order;

import com.techx7.techstore.model.dto.user.BillingInfoDTO;

public class OrderDTO extends BillingInfoDTO {

    private String cartItems;

    private String payment;

    public OrderDTO() {}

    public String getCartItems() {
        return cartItems;
    }

    public void setCartItems(String cartItems) {
        this.cartItems = cartItems;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

}
