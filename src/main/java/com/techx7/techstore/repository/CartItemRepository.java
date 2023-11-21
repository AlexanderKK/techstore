package com.techx7.techstore.repository;

import com.techx7.techstore.model.entity.CartItem;
import com.techx7.techstore.model.entity.Product;
import com.techx7.techstore.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<List<CartItem>> findAllByUser(User user);

    CartItem findByUserAndProduct(User user, Product product);

    @Query("UPDATE CartItem c SET c.quantity = ?1 WHERE c.user.uuid = ?2 AND c.product.uuid = ?3")
    @Modifying
    void updateQuantity(Integer quantity, UUID userUuid, UUID productUuid);

    void deleteByProductAndUser(Product product, User user);

}
