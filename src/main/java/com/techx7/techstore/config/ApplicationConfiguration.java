package com.techx7.techstore.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.model.dto.cart.CartItemDTO;
import com.techx7.techstore.model.dto.category.AddCategoryDTO;
import com.techx7.techstore.model.dto.category.CategoryDTO;
import com.techx7.techstore.model.dto.manufacturer.AddManufacturerDTO;
import com.techx7.techstore.model.dto.manufacturer.ManufacturerDTO;
import com.techx7.techstore.model.dto.manufacturer.ManufacturerWithModelsDTO;
import com.techx7.techstore.model.dto.model.AddModelDTO;
import com.techx7.techstore.model.dto.model.ModelDTO;
import com.techx7.techstore.model.dto.model.ModelWithManufacturerDTO;
import com.techx7.techstore.model.dto.product.AddProductDTO;
import com.techx7.techstore.model.dto.product.ProductCartItemDTO;
import com.techx7.techstore.model.dto.product.ProductDTO;
import com.techx7.techstore.model.dto.product.ProductDetailsDTO;
import com.techx7.techstore.model.dto.role.AddRoleDTO;
import com.techx7.techstore.model.dto.role.RoleDTO;
import com.techx7.techstore.model.dto.user.RegisterDTO;
import com.techx7.techstore.model.dto.user.UserDTO;
import com.techx7.techstore.model.dto.user.UserProfileDTO;
import com.techx7.techstore.model.entity.*;
import com.techx7.techstore.model.enums.GenderEnum;
import com.techx7.techstore.repository.CategoryRepository;
import com.techx7.techstore.repository.ManufacturerRepository;
import com.techx7.techstore.repository.ModelRepository;
import com.techx7.techstore.repository.RoleRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.Provider;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static com.techx7.techstore.constant.Messages.ENTITY_NOT_FOUND;
import static com.techx7.techstore.utils.DiscountUtils.setProductDiscountPrice;
import static com.techx7.techstore.utils.StringUtils.capitalize;

@Configuration
public class ApplicationConfiguration {

    private final ManufacturerRepository manufacturerRepository;
    private final CategoryRepository categoryRepository;
    private final ModelRepository modelRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationConfiguration(ManufacturerRepository manufacturerRepository,
                                    CategoryRepository categoryRepository,
                                    ModelRepository modelRepository,
                                    RoleRepository roleRepository,
                                    PasswordEncoder passwordEncoder) {
        this.manufacturerRepository = manufacturerRepository;
        this.categoryRepository = categoryRepository;
        this.modelRepository = modelRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public Gson createGson() {
        return new GsonBuilder().setPrettyPrinting().create();
    }

    @Bean
    public ModelMapper createMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        modelMapper.getConfiguration().setFieldMatchingEnabled(true);

        // Trim String
        Converter<String, String> trimStringConverter
                = context -> context.getSource() == null
                ? null
                : context.getSource().trim();

        modelMapper.addConverter(trimStringConverter);

        // LocalDateTime -> String
        Converter<LocalDateTime, String> localDateTimeToString
                = context -> context.getSource() == null
                ? null
                : context.getSource().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm:ss"));

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

        // Category -> CategoryDTO
        modelMapper
                .createTypeMap(Category.class, CategoryDTO.class)
                .addMappings(mapper -> mapper
                        .using(localDateTimeToString)
                        .map(Category::getCreated, CategoryDTO::setCreated))
                .addMappings(mapper -> mapper
                        .using(localDateTimeToString)
                        .map(Category::getModified, CategoryDTO::setModified));

        // AddManufacturerDTO -> Manufacturer
        modelMapper
                .createTypeMap(AddManufacturerDTO.class, Manufacturer.class)
                .addMappings(mapper -> mapper
                        .using(toImageUrl)
                        .map(AddManufacturerDTO::getImage, Manufacturer::setImageUrl));

        // Manufacturer -> ManufacturerDTO
        modelMapper
                .createTypeMap(Manufacturer.class, ManufacturerDTO.class)
                .addMappings(mapper -> mapper
                        .using(localDateTimeToString)
                        .map(Manufacturer::getCreated, ManufacturerDTO::setCreated))
                .addMappings(mapper -> mapper
                        .using(localDateTimeToString)
                        .map(Manufacturer::getModified, ManufacturerDTO::setModified));

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
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "Manufacturer")));

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

        Converter<Manufacturer, Long> toManufacturerId
                = context -> context.getSource() == null
                ? null
                : modelMapper.map(context.getSource(), Long.class);

        modelMapper
                .createTypeMap(Model.class, ModelWithManufacturerDTO.class)
                .addMappings(mapper -> mapper
                        .using(toModelManufacturerDTO)
                        .map(Model::getManufacturer, ModelWithManufacturerDTO::setManufacturerDTO))
                .addMappings(mapper -> mapper
                        .using(toManufacturerId)
                        .map(model -> model.getManufacturer().getId(), ModelWithManufacturerDTO::setManufacturerId))
                .addMappings(mapper -> mapper
                        .using(localDateTimeToString)
                        .map(Model::getCreated, ModelWithManufacturerDTO::setCreated))
                .addMappings(mapper -> mapper
                        .using(localDateTimeToString)
                        .map(Model::getModified, ModelWithManufacturerDTO::setModified));

        // AddProductDTO -> Product
        Converter<String, Set<Category>> toCategoriesSet
                = context -> context.getSource() == null
                ? null
                : Arrays.stream(context.getSource().split(","))
                    .mapToLong(Long::parseLong)
                    .mapToObj(categoryId -> categoryRepository.findById(categoryId)
                            .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "Category"))))
                    .collect(Collectors.toSet());

        Converter<Long, Model> toModel
                = context -> context.getSource() == null
                ? null
                : modelRepository.findById(context.getSource())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "Model")));

        Converter<String, Integer> toAvailableQuantity
                = context -> context.getSource() == null
                ? null
                : Integer.parseInt(context.getSource());

        modelMapper.createTypeMap(AddProductDTO.class, Product.class)
                .addMappings(mapper -> mapper
                        .using(toImageUrl)
                        .map(AddProductDTO::getImage, Product::setImageUrl))
                .addMappings(mapper -> mapper
                        .using(toCategoriesSet)
                        .map(AddProductDTO::getCategories, Product::setCategories))
                .addMappings(mapper -> mapper
                        .using(toModel)
                        .map(AddProductDTO::getModel, Product::setModel))
                .addMappings(mapper -> mapper
                        .using(toAvailableQuantity)
                        .map(AddProductDTO::getInitialQuantity, Product::setAvailableQuantity))
                .addMappings(
                        new PropertyMap<AddProductDTO, Product>() {
                            @Override
                            protected void configure() {
                                using(ctx -> setProductDiscountPrice((AddProductDTO) ctx.getSource()))
                                        .map(source, destination.getDiscountPrice());
                            }
                        });

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
                        .map(Product::getDiscountPrice, ProductDTO::setDiscountPrice));

        // RegisterDTO -> User
        Provider<User> newUserWithRoleProvider = req -> {
            Role role = roleRepository.findByName("USER")
                    .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "Role")));

            User user = new User();
            user.setRoles(Set.of(role));

            return user;
        };

        Provider<String> encodedPasswordProvider =
                request -> passwordEncoder.encode(String.valueOf(request.getSource()));

        modelMapper
                .createTypeMap(RegisterDTO.class, User.class)
                .setProvider(newUserWithRoleProvider)
                .addMappings(mapper -> mapper
                        .with(encodedPasswordProvider)
                        .map(RegisterDTO::getPassword, User::setPassword));

        // UserInfo -> UserProfileDTO
        Converter<GenderEnum, String> toGenderName
                = context -> context.getSource() == null
                ? null
                : capitalize(context.getSource().name());

        Converter<Country, String> toCountryName
                = context -> context.getSource() == null
                ? null
                : context.getSource().getName();

        modelMapper
                .createTypeMap(UserInfo.class, UserProfileDTO.class)
                .addMappings(mapper -> mapper
                        .using(toGenderName)
                        .map(UserInfo::getGender, UserProfileDTO::setGender))
                .addMappings(mapper -> mapper
                        .using(toCountryName)
                        .map(UserInfo::getCountry, UserProfileDTO::setCountry));

        // User -> UserDTO
        Converter<Set<Role>, String> toRoleIds
                = context -> context.getSource() == null
                ? null
                : context.getSource().stream()
                    .map(BaseEntity::getId)
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

        Converter<UserInfo, UserProfileDTO> toUserProfileDTO
                = context -> context.getSource() == null
                ? null
                : modelMapper.map(context.getSource(), UserProfileDTO.class);

        modelMapper
                .createTypeMap(User.class, UserDTO.class)
                .addMappings(mapper -> mapper
                        .using(localDateTimeToString)
                        .map(User::getCreated, UserDTO::setCreated))
                .addMappings(mapper -> mapper
                        .using(localDateTimeToString)
                        .map(User::getModified, UserDTO::setModified))
                .addMappings(mapper -> mapper
                        .using(toRoleIds)
                        .map(User::getRoles, UserDTO::setRoles));

        // Role -> RoleDTO
        Provider<String> roleNameProvider =
                request -> capitalize(String.valueOf(request.getSource()));

        modelMapper
                .createTypeMap(Role.class, RoleDTO.class)
                .addMappings(mapper -> mapper
                        .with(roleNameProvider)
                        .map(Role::getName, RoleDTO::setName));

        // AddRoleDTO -> Role
        modelMapper
                .createTypeMap(AddRoleDTO.class, Role.class)
                .addMappings(mapper -> mapper
                        .using(toImageUrl)
                        .map(AddRoleDTO::getImage, Role::setImageUrl));

        // Product -> ProductCartItemDTO
        Converter<Model, String> toProductName = context -> {
            Model source = context.getSource();

            if (source == null) {
                return null;
            } else {
                return source.getManufacturer().getName() + " " + source.getName();
            }
        };

        modelMapper
                .createTypeMap(Product.class, ProductCartItemDTO.class)
                .addMappings(mapper -> mapper
                        .using(toProductName)
                        .map(Product::getModel, ProductCartItemDTO::setLink));

        Converter<Product, ProductCartItemDTO> toProductCartItemDTO
                = context -> context.getSource() == null
                ? null
                : modelMapper.map(context.getSource(), ProductCartItemDTO.class);

        modelMapper
                .createTypeMap(CartItem.class, CartItemDTO.class)
                .addMappings(mapper -> mapper
                        .using(toProductCartItemDTO)
                        .map(CartItem::getProduct, CartItemDTO::setProductDTO));

        //Product -> ProductDetailsDTO
        modelMapper
                .createTypeMap(Product.class, ProductDetailsDTO.class)
                .addMappings(mapper -> mapper
                        .using(toCategoryNamesSet)
                        .map(Product::getCategories, ProductDetailsDTO::setCategories))
                .addMappings(mapper -> mapper
                        .using(toManufacturerName)
                        .map(product -> product.getModel().getManufacturer(), ProductDetailsDTO::setManufacturer))
                .addMappings(mapper -> mapper
                        .using(toModelName)
                        .map(Product::getModel, ProductDetailsDTO::setModel))
                .addMappings(mapper -> mapper
                        .using(toProductName)
                        .map(Product::getModel, ProductDetailsDTO::setName));

        return modelMapper;
    }

}
