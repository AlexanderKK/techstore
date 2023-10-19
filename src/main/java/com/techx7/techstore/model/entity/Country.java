package com.techx7.techstore.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "countries")
public class Country extends BaseEntity {

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Size(max = 2)
    @Column(nullable = false, columnDefinition = "CHAR(2) DEFAULT '' COMMENT 'ISO 3661-1 alpha-2'")
    private String abv;

    @NotBlank
    @Size(max = 3)
    @Column(columnDefinition = "CHAR(3) DEFAULT NULL COMMENT 'ISO 3661-1 alpha-3'")
    private String abv3;

    @NotBlank
    @Size(max = 3)
    @Column(columnDefinition = "CHAR(3) DEFAULT NULL")
    private String abv3_alt;

    @NotBlank
    @Size(max = 3)
    @Column(columnDefinition = "CHAR(3) DEFAULT NULL COMMENT 'ISO 3661-1 numeric'")
    private String code;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, unique = true)
    private String slug;

    public Country() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbv() {
        return abv;
    }

    public void setAbv(String abv) {
        this.abv = abv;
    }

    public String getAbv3() {
        return abv3;
    }

    public void setAbv3(String abv3) {
        this.abv3 = abv3;
    }

    public String getAbv3_alt() {
        return abv3_alt;
    }

    public void setAbv3_alt(String abv3_alt) {
        this.abv3_alt = abv3_alt;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

}
