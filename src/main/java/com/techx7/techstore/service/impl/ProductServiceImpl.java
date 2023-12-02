package com.techx7.techstore.service.impl;

import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.model.dto.product.AddProductDTO;
import com.techx7.techstore.model.dto.product.ProductDTO;
import com.techx7.techstore.model.dto.product.ProductDetailsDTO;
import com.techx7.techstore.model.entity.Product;
import com.techx7.techstore.repository.ProductRepository;
import com.techx7.techstore.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static com.techx7.techstore.constant.Messages.ENTITY_NOT_FOUND;
import static com.techx7.techstore.utils.FileUtils.uploadFile;
import static com.techx7.techstore.utils.StringUtils.getClassNameLowerCase;

@Service
public class ProductServiceImpl implements ProductService {

    private final ModelMapper mapper;
    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ModelMapper mapper,
                              ProductRepository productRepository) {
        this.mapper = mapper;
        this.productRepository = productRepository;
    }

    @Override
    public void createProduct(AddProductDTO addProductDTO) throws IOException {
        Product product = mapper.map(addProductDTO, Product.class);

        uploadFile(
                addProductDTO.getImage(),
                getClassNameLowerCase(Product.class),
                getProductNameToLowerCase(product)
        );

        productRepository.save(product);
    }

    @Override
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        System.out.println("Displaying products");

        Page<ProductDTO> products = productRepository.findAll(pageable)
                .map(toProductDTO());
        return products;
    }

    @Override
    public List<ProductDTO> getAllProductsWithDiscount() {
        return productRepository.findAll().stream()
                .map(toProductDTO())
                .filter(productDTO -> productDTO.getDiscountPrice() != null)
                .filter(productDTO -> productDTO.getDiscountPrice().compareTo(BigDecimal.ZERO) >= 0)
                .sorted(Comparator.comparing(ProductDTO::getDiscountPrice))
                .toList();
    }

    @Override
    public Page<ProductDTO> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();

        List<ProductDTO> productDTOs = getAllProducts(pageable).stream().toList();

        List<ProductDTO> resultList = productDTOs.subList(0, productDTOs.size());

        Page<ProductDTO> productPage =
                new PageImpl<>(resultList, PageRequest.of(currentPage, pageSize), productDTOs.size());

        return productPage;
    }

    @Override
    public ProductDTO getProductByUuid(UUID uuid) {
        return productRepository.findByUuid(uuid)
                .map(product -> mapper.map(product, ProductDTO.class))
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "Product")));
    }

    @Override
    public ProductDetailsDTO getProductDetailsByUuid(UUID uuid) {
        return productRepository.findByUuid(uuid)
                .map(toProductDetailsDTO())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "Product")));
    }

    private Function<Product, ProductDetailsDTO> toProductDetailsDTO() {
        return product -> {
            ProductDetailsDTO productDetailsDTO = mapper.map(product, ProductDetailsDTO.class);

            return productDetailsDTO;
        };
    }

    private Function<Product, ProductDTO> toProductDTO() {
        return product -> {
            ProductDTO productDTO = mapper.map(product, ProductDTO.class);

            return productDTO;
        };
    }

    private static String getProductNameToLowerCase(Product product) {
        return (product.getModel().getManufacturer().getName() +
                " " +
                product.getModel().getName() +
                " " +
                product.getUuid()).toLowerCase();
    }

}
