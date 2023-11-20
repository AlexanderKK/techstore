package com.techx7.techstore.repository;

import com.techx7.techstore.model.entity.Category;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String value);

    void deleteByUuid(UUID uuid);

    Optional<Category> findByUuid(UUID uuid);

}
