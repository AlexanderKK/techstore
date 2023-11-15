package com.techx7.techstore.repository;

import com.techx7.techstore.model.entity.UserActivationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserActivationCodeRepository extends JpaRepository<UserActivationCode, Long> {

    Optional<UserActivationCode> findByActivationCode(String activationCode);

}
