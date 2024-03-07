package com.techx7.techstore.model.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity(name = "user_metadata")
public class UserMetadata extends BaseEntity {

    @NotNull(message = "IP address should not be empty")
    @Column(name = "ip_address", nullable = false)
    private String ip;

    @NotNull(message = "IP address should not be empty")
    @Column(name = "user_agent", nullable = false)
    private String userAgent;

    @Column(name = "is_restricted")
    private boolean isRestricted;

    @ManyToMany
    @JoinTable(name = "users_metadata",
            joinColumns = @JoinColumn(name = "user_metadata_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @Valid
    private List<User> users;

    public UserMetadata() {}

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public boolean isRestricted() {
        return isRestricted;
    }

    public void setRestricted(boolean restricted) {
        isRestricted = restricted;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

}
