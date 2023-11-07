package com.techx7.techstore.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.techx7.techstore.exception.ManufacturerNotFoundException;
import com.techx7.techstore.model.dto.category.AddCategoryDTO;
import com.techx7.techstore.model.dto.manufacturer.AddManufacturerDTO;
import com.techx7.techstore.model.dto.model.AddModelDTO;
import com.techx7.techstore.model.dto.product.ProductDTO;
import com.techx7.techstore.model.entity.Category;
import com.techx7.techstore.model.entity.Manufacturer;
import com.techx7.techstore.model.entity.Model;
import com.techx7.techstore.model.entity.Product;
import com.techx7.techstore.repository.ManufacturerRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Collectors;

import static com.techx7.techstore.constant.Messages.MANUFACTURER_NOT_FOUND;

@Configuration
public class ApplicationConfiguration {

    private final ManufacturerRepository manufacturerRepository;

    @Autowired
    public ApplicationConfiguration(ManufacturerRepository manufacturerRepository) {
        this.manufacturerRepository = manufacturerRepository;
    }

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
                = context -> context.getSource() == null
                ? null
                : context.getSource().stream().map(Category::getName).collect(Collectors.toSet());

        Converter<Manufacturer, String> toManufacturerName
                = context -> context.getSource() == null
                ? null
                : context.getSource().getName();

        Converter<Model, String> toModelName
                = context -> context.getSource() == null
                ? null
                : context.getSource().getName();

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
                        .map(Product::getModel, ProductDTO::setModel))
                .addMappings(mapper -> mapper
                        .map(Product::getDiscountPercentage, ProductDTO::setDiscountPrice));

        // TODO: CategoryDTO -> Category
        Converter<MultipartFile, String> toImageUrl
                = context -> context.getSource() == null
                ? null
                : context.getSource().getOriginalFilename();

        modelMapper
                .createTypeMap(AddCategoryDTO.class, Category.class)
                .addMappings(mapper -> mapper
                        .using(toImageUrl)
                        .map(AddCategoryDTO::getImage, Category::setImageUrl));

        // TODO: AddManufacturerDTO -> Manufacturer
        modelMapper
                .createTypeMap(AddManufacturerDTO.class, Manufacturer.class)
                .addMappings(mapper -> mapper
                        .using(toImageUrl)
                        .map(AddManufacturerDTO::getImage, Manufacturer::setImageUrl));

        // TODO: AddModelDTO -> Model
        Converter<Long, Manufacturer> toManufacturer
                = context -> context.getSource() == null
                ? null
                : manufacturerRepository.findById(context.getSource())
                .orElseThrow(() -> new ManufacturerNotFoundException(MANUFACTURER_NOT_FOUND));

        modelMapper
                .createTypeMap(AddModelDTO.class, Model.class)
                .addMappings(mapper -> mapper
                        .using(toManufacturer)
                        .map(AddModelDTO::getManufacturer, Model::setManufacturer));

        return modelMapper;
    }

}
