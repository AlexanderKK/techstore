package com.techx7.techstore.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Calendar;

@Entity
@Table(name = "models")
public class Model extends BaseEntity {

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotNull
    @ManyToOne(optional = false)
    private Manufacturer manufacturer;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private Calendar created = Calendar.getInstance();

    @Column(columnDefinition = "TIMESTAMP")
    private Calendar modified;

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

}
