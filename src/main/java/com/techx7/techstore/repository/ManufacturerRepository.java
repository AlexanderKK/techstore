package com.techx7.techstore.repository;

import com.techx7.techstore.model.entity.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {

    Optional<Manufacturer> findByName(String value);

    void deleteByUuid(UUID uuid);

    Optional<Manufacturer> findByUuid(UUID uuid);

}
