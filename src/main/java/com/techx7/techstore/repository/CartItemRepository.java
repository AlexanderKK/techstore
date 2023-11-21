package com.techx7.techstore.repository;

import com.techx7.techstore.model.entity.CartItem;
import com.techx7.techstore.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findAllByUser(User user);

}
