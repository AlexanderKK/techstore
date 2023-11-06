package com.techx7.techstore.service;

import com.techx7.techstore.model.dto.product.AddProductDTO;
import com.techx7.techstore.model.dto.product.ProductDTO;

import java.util.List;

public interface ProductService {

    void createProduct(AddProductDTO addProductDTO);

    List<ProductDTO> getAllProducts();

}
