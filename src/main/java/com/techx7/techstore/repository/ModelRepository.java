package com.techx7.techstore.repository;

import com.techx7.techstore.model.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {

    void deleteByUuid(UUID uuid);

    Optional<Model> findByUuid(UUID uuid);

    Optional<Model> findByManufacturerId(Long manufacturerId);

    List<Model> findAllByManufacturerId(Long id);

}
