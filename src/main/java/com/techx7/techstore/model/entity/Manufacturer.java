package com.techx7.techstore.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Set;

@Entity
@Table(name = "manufacturers")
public class Manufacturer extends BaseEntity {

    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String description;

    @Column(name = "image_url")
    @Size(min = 8, max = 512)
    private String imageUrl;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private Calendar created = Calendar.getInstance();

    @Column(columnDefinition = "TIMESTAMP")
    private Calendar modified;

    @OneToMany(mappedBy = "manufacturer", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<Model> models;

    public Manufacturer() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public Set<Model> getModels() {
        return models;
    }

    public void setModels(Set<Model> models) {
        this.models = models;
    }

}
