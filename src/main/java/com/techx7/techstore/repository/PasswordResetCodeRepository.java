package com.techx7.techstore.repository;

import com.techx7.techstore.model.entity.PasswordResetCode;
import com.techx7.techstore.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetCodeRepository extends JpaRepository<PasswordResetCode, Long> {

    Optional<PasswordResetCode> findByPasswordResetCode(String resetCode);

    Optional<PasswordResetCode> findByPasswordResetCodeAndUser(String resetCode, User user);

}
