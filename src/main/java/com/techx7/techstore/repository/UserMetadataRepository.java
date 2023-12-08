package com.techx7.techstore.repository;

import com.techx7.techstore.model.entity.UserMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserMetadataRepository extends JpaRepository<UserMetadata, Long> {

    Optional<UserMetadata> findByIp(String ip);

    Optional<UserMetadata> findByIpAndUserAgent(String ip, String userAgent);

    List<UserMetadata> findAllByIp(String ipAddress);

}
