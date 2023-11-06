package com.techx7.techstore.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.techx7.techstore.model.dto.product.ProductDTO;
import com.techx7.techstore.model.entity.Category;
import com.techx7.techstore.model.entity.Manufacturer;
import com.techx7.techstore.model.entity.Model;
import com.techx7.techstore.model.entity.Product;
import org.modelmapper.Conditions;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public Gson createGson() {
        return new GsonBuilder().setPrettyPrinting().create();
    }

    @Bean
    public ModelMapper createMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addConverter((Converter<String, LocalDateTime>) mappingContext ->
                LocalDateTime.parse(mappingContext.getSource(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        // TODO: Product -> ProductDTO
        Converter<Set<Category>, Set<String>> toCategoryNamesSet
                = ctx -> (ctx.getSource() == null)
                ? null
                : ctx.getSource().stream().map(Category::getName).collect(Collectors.toSet());

        Converter<Manufacturer, String> toManufacturerName
                = ctx -> (ctx.getSource() == null)
                ? null
                : ctx.getSource().getName();

        Converter<Model, String> toModelName
                = ctx -> (ctx.getSource() == null)
                ? null
                : ctx.getSource().getName();

        modelMapper
                .createTypeMap(Product.class, ProductDTO.class)
                .addMappings(mapper -> mapper
                        .using(toCategoryNamesSet)
                        .map(Product::getCategories, ProductDTO::setCategories))
                .addMappings(mapper -> mapper
                        .using(toManufacturerName)
                        .map(product -> product.getModel().getManufacturer(), ProductDTO::setManufacturer))
                .addMappings(mapper -> mapper
                        .using(toModelName)
                        .map(Product::getModel, ProductDTO::setModel));

        return modelMapper;
    }

}
