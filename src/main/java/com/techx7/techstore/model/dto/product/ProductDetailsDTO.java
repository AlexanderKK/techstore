package com.techx7.techstore.model.dto.product;

import com.techx7.techstore.model.util.KeyValuePair;
import jakarta.validation.constraints.NotBlank;

import java.util.Arrays;
import java.util.List;

public class ProductDetailsDTO extends ProductDTO {

    private String description;

    @NotBlank(message = "Please add technical characteristics")
    private String specification;

    @NotBlank(message = "Please add model and manufacturer")
    private String name;

    public ProductDetailsDTO() {}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? description : description.trim();
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    public List<KeyValuePair> getSpecificationsList() {
        return Arrays.stream(getSpecification().split("\n|\r\n"))
                .map(spec -> {
                    String[] line = spec.split("\\s*:\\s*");

                    String key = line[0];
                    String value = line[1];

                    return new KeyValuePair(key, value);
                })
                .toList();
    }

}
