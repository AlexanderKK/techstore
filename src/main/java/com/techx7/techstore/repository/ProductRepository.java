package com.techx7.techstore.repository;

import com.techx7.techstore.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByModelId(Long modelId);

    Optional<Product> findByUuid(UUID uuid);

}
