package com.techx7.techstore.service.impl;

import com.techx7.techstore.model.dto.product.AddProductDTO;
import com.techx7.techstore.model.dto.product.ProductDTO;
import com.techx7.techstore.model.entity.Product;
import com.techx7.techstore.repository.ProductRepository;
import com.techx7.techstore.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import static com.techx7.techstore.util.FileUtils.saveFileLocally;

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
        saveFileLocally(addProductDTO.getImage());

        Product product = mapper.map(addProductDTO, Product.class);

        productRepository.save(product);
    }

    @Override
    @Cacheable("products")
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        System.out.println("Displaying products");

        return productRepository.findAll(pageable)
                .map(toProductDTO());
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

    private Function<Product, ProductDTO> toProductDTO() {
        return product -> {
            ProductDTO productDTO = mapper.map(product, ProductDTO.class);

            setDiscountPrice(productDTO);

            return productDTO;
        };
    }

}
