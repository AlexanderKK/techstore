package com.techx7.techstore.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "user_activation_code")
public class UserActivationCode extends BaseEntity {

    @NotNull(message = "Should not be empty")
    @Column(nullable = false)
    private String activationCode;

    @Column(name="timestamp", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @CreationTimestamp
    private LocalDateTime created;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @Column
    private Instant modified;

    @NotNull(message = "Should not be empty")
    @ManyToOne(optional = false)
    private User user;

    public UserActivationCode() {}

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public Instant getModified() {
        return modified;
    }

    public void setModified(Instant modified) {
        this.modified = modified;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
