package com.techx7.techstore.repository;

import com.techx7.techstore.model.entity.UserInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ContextConfiguration(classes = {UserInfoRepository.class})
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.techx7.techstore.model.entity"})
@DataJpaTest
class UserInfoRepositoryTest {

    @Autowired
    private UserInfoRepository userInfoRepository;

    private UserInfo savedUserInfo;

    private Long existingUserInfoId;

    @BeforeEach
    void setUp() {
        userInfoRepository.deleteAll();

        UserInfo userInfo = new UserInfo();
        userInfo.setFirstName("John");
        userInfo.setLastName("Doe");
        userInfo.setPhoneNumber("1234567890");

        savedUserInfo = userInfoRepository.save(userInfo);
        existingUserInfoId = savedUserInfo.getId();
    }

    @AfterEach
    void tearDown() {
        userInfoRepository.deleteAll();
    }

    @Test
    void testFindByIdWhenEntityExists() {
        // Arrange and Act
        Optional<UserInfo> foundUserInfo = userInfoRepository.findById(existingUserInfoId);

        // Assert
        assertTrue(foundUserInfo.isPresent());
        assertThat(foundUserInfo.get()).isEqualTo(savedUserInfo);
    }

    @Test
    void testFindByNameWhenEntityDoesNotExist() {
        // Arrange
        Long id = 0L;

        // Act
        Optional<UserInfo> foundUserInfo = userInfoRepository.findById(id);

        // Assert
        assertTrue(foundUserInfo.isEmpty());
    }

}
