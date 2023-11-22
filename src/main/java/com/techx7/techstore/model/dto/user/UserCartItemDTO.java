package com.techx7.techstore.model.dto.user;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class UserCartItemDTO {

    @NotNull(message = "UUID should not be empty")
    private UUID uuid;

    public UserCartItemDTO() {}

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

}
