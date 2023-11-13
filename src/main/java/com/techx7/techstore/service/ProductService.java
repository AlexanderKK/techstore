package com.techx7.techstore.service;

import com.techx7.techstore.model.dto.product.AddProductDTO;
import com.techx7.techstore.model.dto.product.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    void createProduct(AddProductDTO addProductDTO);

    Page<ProductDTO> getAllProducts(Pageable pageable);

    List<ProductDTO> getAllProductsWithDiscount();

    Page<ProductDTO> findPaginated(Pageable pageable);

}
