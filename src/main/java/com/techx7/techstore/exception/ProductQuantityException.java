package com.techx7.techstore.exception;

public class ProductQuantityException extends RuntimeException {

    private final Integer availableQuantity;

    public ProductQuantityException(String message, Integer availableQuantity) {
        super(message);
        this.availableQuantity = availableQuantity;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

}
