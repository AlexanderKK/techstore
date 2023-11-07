package com.techx7.techstore.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.techx7.techstore.exception.CategoryNotFoundException;
import com.techx7.techstore.exception.ManufacturerNotFoundException;
import com.techx7.techstore.exception.ModelNotFoundException;
import com.techx7.techstore.model.dto.category.AddCategoryDTO;
import com.techx7.techstore.model.dto.manufacturer.AddManufacturerDTO;
import com.techx7.techstore.model.dto.manufacturer.ManufacturerDTO;
import com.techx7.techstore.model.dto.manufacturer.ManufacturerWithModelsDTO;
import com.techx7.techstore.model.dto.model.AddModelDTO;
import com.techx7.techstore.model.dto.model.ModelDTO;
import com.techx7.techstore.model.dto.model.ModelWithManufacturerDTO;
import com.techx7.techstore.model.dto.product.AddProductDTO;
import com.techx7.techstore.model.dto.product.ProductDTO;
import com.techx7.techstore.model.entity.Category;
import com.techx7.techstore.model.entity.Manufacturer;
import com.techx7.techstore.model.entity.Model;
import com.techx7.techstore.model.entity.Product;
import com.techx7.techstore.repository.CategoryRepository;
import com.techx7.techstore.repository.ManufacturerRepository;
import com.techx7.techstore.repository.ModelRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static com.techx7.techstore.constant.Messages.*;

@Configuration
public class ApplicationConfiguration {

    private final ManufacturerRepository manufacturerRepository;
    private final CategoryRepository categoryRepository;
    private final ModelRepository modelRepository;

    @Autowired
    public ApplicationConfiguration(ManufacturerRepository manufacturerRepository,
                                    CategoryRepository categoryRepository,
                                    ModelRepository modelRepository) {
        this.manufacturerRepository = manufacturerRepository;
        this.categoryRepository = categoryRepository;
        this.modelRepository = modelRepository;
    }

    @Bean
    public Gson createGson() {
        return new GsonBuilder().setPrettyPrinting().create();
    }

    @Bean
    public ModelMapper createMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // AddCategoryDTO -> Category
        Converter<MultipartFile, String> toImageUrl
                = context -> context.getSource() == null
                ? null
                : context.getSource().getOriginalFilename();

        modelMapper
                .createTypeMap(AddCategoryDTO.class, Category.class)
                .addMappings(mapper -> mapper
                        .using(toImageUrl)
                        .map(AddCategoryDTO::getImage, Category::setImageUrl));

        // AddManufacturerDTO -> Manufacturer
        modelMapper
                .createTypeMap(AddManufacturerDTO.class, Manufacturer.class)
                .addMappings(mapper -> mapper
                        .using(toImageUrl)
                        .map(AddManufacturerDTO::getImage, Manufacturer::setImageUrl));

        // Manufacturer -> ManufacturerWithModelsDTO
        Converter<Set<Model>, Set<ModelDTO>> toModelDTO
                = context -> context.getSource() == null
                ? null
                : context.getSource().stream().map(model -> modelMapper.map(model, ModelDTO.class)).collect(Collectors.toSet());

        modelMapper
                .createTypeMap(Manufacturer.class, ManufacturerWithModelsDTO.class)
                .addMappings(mapper -> mapper
                        .using(toModelDTO)
                        .map(Manufacturer::getModels, ManufacturerWithModelsDTO::setModelDTOs));

        // AddModelDTO -> Model
        Converter<Long, Manufacturer> toManufacturer
                = context -> context.getSource() == null
                ? null
                : manufacturerRepository.findById(context.getSource())
                .orElseThrow(() -> new ManufacturerNotFoundException(MANUFACTURER_NOT_PRESENT));

        modelMapper
                .createTypeMap(AddModelDTO.class, Model.class)
                .addMappings(mapper -> mapper
                        .using(toManufacturer)
                        .map(AddModelDTO::getManufacturer, Model::setManufacturer));

        // Model -> ModelWithManufacturerDTO
        Converter<Manufacturer, ManufacturerDTO> toModelManufacturerDTO
                = context -> context.getSource() == null
                ? null
                : modelMapper.map(context.getSource(), ManufacturerDTO.class);

        modelMapper
                .createTypeMap(Model.class, ModelWithManufacturerDTO.class)
                .addMappings(mapper -> mapper
                        .using(toModelManufacturerDTO)
                        .map(Model::getManufacturer, ModelWithManufacturerDTO::setManufacturerDTO));

        // AddProductDTO -> Product
        Converter<String, Set<Category>> toCategoriesSet
                = context -> context.getSource() == null
                ? null
                : Arrays.stream(context.getSource().split(","))
                .mapToLong(Long::parseLong)
                .mapToObj(categoryId -> categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new CategoryNotFoundException(CATEGORY_NOT_PRESENT)))
                .collect(Collectors.toSet());

        Converter<Long, Model> toModel
                = context -> context.getSource() == null
                ? null
                : modelRepository.findById(context.getSource())
                .orElseThrow(() -> new ModelNotFoundException(MODEL_NOT_PRESENT));

        modelMapper.createTypeMap(AddProductDTO.class, Product.class)
                .addMappings(mapper -> mapper
                        .using(toImageUrl)
                        .map(AddProductDTO::getImage, Product::setImageUrl))
                .addMappings(mapper -> mapper
                        .using(toCategoriesSet)
                        .map(AddProductDTO::getCategories, Product::setCategories))
                .addMappings(mapper -> mapper
                        .using(toModel)
                        .map(AddProductDTO::getModel, Product::setModel));

        // Product -> ProductDTO
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

        return modelMapper;
    }

}
