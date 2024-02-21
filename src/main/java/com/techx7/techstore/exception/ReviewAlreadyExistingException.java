package com.techx7.techstore.exception;

import java.util.UUID;

public class ReviewAlreadyExistingException extends RuntimeException {

    private UUID productUuid;

    public ReviewAlreadyExistingException(String message, UUID productUuid) {
        super(message);
        this.productUuid = productUuid;
    }

    public UUID getProductUuid() {
        return productUuid;
    }

}
