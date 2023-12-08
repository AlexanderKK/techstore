package com.techx7.techstore.repository;

import com.techx7.techstore.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

    void deleteByUuid(UUID uuid);

    Optional<Role> findByUuid(UUID uuid);

}
