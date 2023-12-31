package com.techx7.techstore.service;

import com.techx7.techstore.model.dto.product.AddProductDTO;
import com.techx7.techstore.model.dto.product.ProductDTO;
import com.techx7.techstore.model.dto.product.ProductDetailsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ProductService {

    void createProduct(AddProductDTO addProductDTO) throws IOException;

    List<ProductDTO> getAllProducts();

    Page<ProductDTO> getAllPageableProducts(Pageable pageable);

    List<ProductDTO> getAllProductsWithDiscount();

    Page<ProductDTO> findPaginated(Pageable pageable);

    ProductDTO getProductByUuid(UUID uuid);

    ProductDetailsDTO getProductDetailsByUuid(UUID uuid);

}
