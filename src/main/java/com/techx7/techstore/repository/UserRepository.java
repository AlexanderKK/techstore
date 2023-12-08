package com.techx7.techstore.repository;

import com.techx7.techstore.model.entity.User;
import com.techx7.techstore.model.entity.UserActivationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String value);

    @Query("SELECT user FROM User user WHERE user.email = :emailOrUsername OR user.username = :emailOrUsername")
    Optional<User> findByEmailOrUsername(String emailOrUsername);

    Optional<User> findByActivationCodesIn(Set<UserActivationCode> activationCodes);

    Optional<User> findByUsername(String username);

    void deleteByUuid(UUID uuid);

    Optional<User> findByUuid(UUID uuid);

}
