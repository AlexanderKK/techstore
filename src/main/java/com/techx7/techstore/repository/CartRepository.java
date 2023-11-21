package com.techx7.techstore.repository;

import com.techx7.techstore.model.dto.product.ProductDTO;
import com.techx7.techstore.model.entity.CartItem;
import com.techx7.techstore.model.entity.Product;
import com.techx7.techstore.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Long> {

    Optional<List<CartItem>> findAllByUser(User user);

    CartItem findByUserAndProduct(User user, Product product);

}
