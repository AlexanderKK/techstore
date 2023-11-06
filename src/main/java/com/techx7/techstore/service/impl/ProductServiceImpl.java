package com.techx7.techstore.service.impl;

import com.techx7.techstore.model.dto.product.AddProductDTO;
import com.techx7.techstore.model.entity.Category;
import com.techx7.techstore.model.entity.Model;
import com.techx7.techstore.model.entity.Product;
import com.techx7.techstore.repository.CategoryRepository;
import com.techx7.techstore.repository.ModelRepository;
import com.techx7.techstore.repository.ProductRepository;
import com.techx7.techstore.service.ProductService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ModelMapper mapper;
    private final ProductRepository productRepository;
    private final ModelRepository modelRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductServiceImpl(ModelMapper mapper,
                              ProductRepository productRepository,
                              ModelRepository modelRepository,
                              CategoryRepository categoryRepository) {
        this.mapper = mapper;
        this.productRepository = productRepository;
        this.modelRepository = modelRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public void createProduct(AddProductDTO addProductDTO) {
        Product product = mapper.map(addProductDTO, Product.class);

        product.setImageUrl(addProductDTO.getImage().getOriginalFilename());

        Set<Category> categories =
                Arrays.stream(addProductDTO.getCategories().split(","))
                .mapToLong(Long::parseLong)
                .mapToObj(categoryId -> categoryRepository.findById(categoryId).orElse(null))
                .collect(Collectors.toSet());
        product.setCategories(categories);

        Optional<Model> model = modelRepository.findById(addProductDTO.getModel());
        model.ifPresent(product::setModel);



        productRepository.save(product);
    }

}
