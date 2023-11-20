package com.techx7.techstore.exception;

import java.util.UUID;

public class ManufacturerNotFoundException extends RuntimeException {

    private final UUID uuid;

    public ManufacturerNotFoundException(String message, UUID uuid) {
        super(message);
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

}
