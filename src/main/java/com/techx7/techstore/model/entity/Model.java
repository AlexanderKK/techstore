package com.techx7.techstore.model.entity;

import com.techx7.techstore.model.dto.model.ModelWithManufacturerDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "models")
public class Model extends BaseEntity {

    @NotBlank(message = "Should not be empty")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "Should not be empty")
    @ManyToOne(optional = false)
    private Manufacturer manufacturer;

    @OneToMany(mappedBy = "model")
    private Set<Product> products;

    @NotNull(message = "Should not be empty")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime created = LocalDateTime.now();

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime modified;

    public Model() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
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

    public void editModel(ModelWithManufacturerDTO modelWithManufacturerDTO) {
        this.setName(modelWithManufacturerDTO.getName());
        this.setModified(LocalDateTime.now());
    }

}
