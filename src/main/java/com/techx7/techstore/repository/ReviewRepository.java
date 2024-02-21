package com.techx7.techstore.repository;

import com.techx7.techstore.model.entity.Product;
import com.techx7.techstore.model.entity.Review;
import com.techx7.techstore.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByUserAndProduct(User user, Product product);

    List<Review> findAllByProduct(Product product);

}
