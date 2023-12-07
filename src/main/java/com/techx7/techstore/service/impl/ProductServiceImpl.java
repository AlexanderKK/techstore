package com.techx7.techstore.service.impl;

import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.model.dto.product.AddProductDTO;
import com.techx7.techstore.model.dto.product.ProductDTO;
import com.techx7.techstore.model.dto.product.ProductDetailsDTO;
import com.techx7.techstore.model.entity.Product;
import com.techx7.techstore.repository.ProductRepository;
import com.techx7.techstore.service.CloudinaryService;
import com.techx7.techstore.service.MonitoringService;
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
import java.util.Objects;
import java.util.UUID;

import static com.techx7.techstore.constant.Messages.ENTITY_NOT_FOUND;

@Service
public class ProductServiceImpl implements ProductService {

    private final ModelMapper mapper;
    private final ProductRepository productRepository;
    private final CloudinaryService cloudinaryService;

    @Autowired
    public ProductServiceImpl(ModelMapper mapper,
                              ProductRepository productRepository,
                              CloudinaryService cloudinaryService,
                              MonitoringService monitoringService) {
        this.mapper = mapper;
        this.productRepository = productRepository;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public void createProduct(AddProductDTO addProductDTO) throws IOException {
        Product product = mapper.map(addProductDTO, Product.class);

        String imageUrl = cloudinaryService.uploadFile(
                addProductDTO.getImage(),
                product.getClass().getSimpleName(),
                product.getModel().getManufacturer().getName() + " " + product.getModel().getName()
        );

        product.setImageUrl(imageUrl);

        productRepository.save(product);
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .filter(product -> Objects.nonNull(product.getModel()))
                .map(product -> mapper.map(product, ProductDTO.class))
                .toList();
    }

    @Override
    public Page<ProductDTO> getAllPageableProducts(Pageable pageable) {
        List<ProductDTO> products = productRepository.findAll(pageable)
                .stream()
                .filter(product -> product.getModel() != null)
                .map(product -> mapper.map(product, ProductDTO.class))
                .toList();

        return new PageImpl<>(products);
    }

    @Override
    public List<ProductDTO> getAllProductsWithDiscount() {
        return productRepository.findAll().stream()
                .map(product -> mapper.map(product, ProductDTO.class))
                .filter(productDTO -> productDTO.getDiscountPrice() != null)
                .filter(productDTO -> productDTO.getDiscountPrice().compareTo(BigDecimal.ZERO) >= 0)
                .sorted(Comparator.comparing(ProductDTO::getDiscountPrice))
                .toList();
    }

    @Override
    public Page<ProductDTO> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();

        List<ProductDTO> productDTOs = getAllPageableProducts(pageable).stream().toList();

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
                .map(product -> mapper.map(product, ProductDetailsDTO.class))
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "Product")));
    }

}
