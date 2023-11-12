package com.techx7.techstore.repository;

import com.techx7.techstore.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String value);

    @Query("SELECT user FROM User user WHERE user.email = :emailOrUsername OR user.username = :emailOrUsername")
    Optional<User> findByEmailOrUsername(String emailOrUsername);

}
