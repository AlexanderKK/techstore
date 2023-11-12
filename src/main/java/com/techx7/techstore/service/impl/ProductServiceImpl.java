package com.techx7.techstore.service.impl;

import com.techx7.techstore.model.dto.product.AddProductDTO;
import com.techx7.techstore.model.dto.product.ProductDTO;
import com.techx7.techstore.model.entity.Product;
import com.techx7.techstore.repository.ProductRepository;
import com.techx7.techstore.service.ProductService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.List;

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

    private void setDiscountPrice(ProductDTO productDTO) {
        if (productDTO.getDiscountPrice() != null &&
                productDTO.getDiscountPrice().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal discountPrice = productDTO.getPrice().multiply(
                    BigDecimal.ONE.subtract(
                            productDTO.getDiscountPrice().divide(
                                    BigDecimal.valueOf(100), RoundingMode.HALF_UP)));

            String formattedDiscountPrice = new DecimalFormat("######.##").format(discountPrice);

            productDTO.setDiscountPrice(new BigDecimal(formattedDiscountPrice));
        }
    }

    @Override
    @Transactional
    public void createProduct(AddProductDTO addProductDTO) {
        Product product = mapper.map(addProductDTO, Product.class);

        productRepository.save(product);
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(product -> {
                    ProductDTO productDTO = mapper.map(product, ProductDTO.class);

                    setDiscountPrice(productDTO);

                    return productDTO;
                })
                .toList();
    }

    @Override
    public List<ProductDTO> getAllProductsWithDiscount() {
        return getAllProducts().stream()
                .filter(productDTO -> productDTO.getDiscountPrice().compareTo(BigDecimal.ZERO) > 0)
                .sorted(Comparator.comparing(ProductDTO::getDiscountPrice))
                .toList();
    }

}
