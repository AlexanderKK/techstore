package com.techx7.techstore.repository;

import com.techx7.techstore.model.entity.Country;
import com.techx7.techstore.model.entity.User;
import com.techx7.techstore.model.entity.UserActivationCode;
import com.techx7.techstore.model.entity.UserInfo;
import com.techx7.techstore.model.enums.GenderEnum;

import java.time.LocalDate;
import java.util.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = {UserRepository.class, UserInfoRepository.class, UserActivationCodeRepository.class})
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.techx7.techstore.model.entity"})
@DataJpaTest(properties = {"spring.main.allow-bean-definition-overriding=true"})
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserInfoRepository userInfoRepository;

  @Autowired
  private UserActivationCodeRepository userActivationCodeRepository;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
    userInfoRepository.deleteAll();
    userActivationCodeRepository.deleteAll();
  }

  /**
   * Method under test: {@link UserRepository#findByEmail(String)}
   */
  @Test
  void testFindByEmail() {
    Country country = createCountry();

    UserInfo userInfo = new UserInfo();
    userInfo.setAddress("42 Main St");
    userInfo.setCity("Oxford");
    userInfo.setCountry(country);
    userInfo.setFirstName("Jane");
    userInfo.setGender(GenderEnum.MALE);
    userInfo.setId(1L);
    userInfo.setImageUrl("https://example.org/example");
    userInfo.setLastName("Doe");
    userInfo.setPhoneNumber("6625550144");
    userInfo.setSecondaryAddress("42 Main St");
    userInfo.setState("MD");
    userInfo.setUuid(UUID.randomUUID());
    userInfo.setZipCode("21654");

    User user = new User();
    user.setActivationCodes(new HashSet<>());
    user.setActive(true);
    user.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    user.setEmail("jane.doe@example.org");
    user.setFailedLoginAttempts(1);
    user.setLastLogin(LocalDate.of(1970, 1, 1).atStartOfDay());
    user.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    user.setPassword("iloveyou");
    user.setRoles(new HashSet<>());
    user.setUserInfo(userInfo);
    user.setUsername("janedoe");
    user.setUuid(UUID.randomUUID());

    Country country2 = new Country();
    country2.setAbv("42");
    country2.setAbv3("42");
    country2.setAbv3_alt("42");
    country2.setCode("42");
    country2.setId(2L);
    country2.setName("42");
    country2.setSlug("42");
    country2.setUuid(UUID.randomUUID());

    UserInfo userInfo2 = new UserInfo();
    userInfo2.setAddress("17 High St");
    userInfo2.setCity("London");
    userInfo2.setCountry(country2);
    userInfo2.setFirstName("John");
    userInfo2.setGender(GenderEnum.FEMALE);
    userInfo2.setId(2L);
    userInfo2.setImageUrl("Image Url");
    userInfo2.setLastName("Smith");
    userInfo2.setPhoneNumber("8605550118");
    userInfo2.setSecondaryAddress("17 High St");
    userInfo2.setState("State");
    userInfo2.setUuid(UUID.randomUUID());
    userInfo2.setZipCode("OX1 1PT");

    User user2 = new User();
    user2.setActivationCodes(new HashSet<>());
    user2.setActive(false);
    user2.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    user2.setEmail("john.smith@example.org");
    user2.setFailedLoginAttempts(0);
    user2.setLastLogin(LocalDate.of(1970, 1, 1).atStartOfDay());
    user2.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    user2.setPassword("Password");
    user2.setRoles(new HashSet<>());
    user2.setUserInfo(userInfo2);
    user2.setUsername("Username");
    user2.setUuid(UUID.randomUUID());

    userInfoRepository.save(userInfo);
    userRepository.save(user);

    userInfoRepository.save(userInfo2);
    userRepository.save(user2);

    userRepository.findByEmail("42");
  }

  /**
   * Method under test: {@link UserRepository#findByEmailOrUsername(String)}
   */
  @Test
  void testFindByEmailOrUsername() {
    Country country = createCountry();

    UserInfo userInfo = new UserInfo();
    userInfo.setAddress("42 Main St");
    userInfo.setCity("Oxford");
    userInfo.setCountry(country);
    userInfo.setFirstName("Jane");
    userInfo.setGender(GenderEnum.MALE);
    userInfo.setId(1L);
    userInfo.setImageUrl("https://example.org/example");
    userInfo.setLastName("Doe");
    userInfo.setPhoneNumber("6625550144");
    userInfo.setSecondaryAddress("42 Main St");
    userInfo.setState("MD");
    userInfo.setZipCode("21654");

    userInfoRepository.save(userInfo);

    User user = new User();
    user.setActivationCodes(new HashSet<>());
    user.setActive(true);
    user.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    user.setEmail("jane.doe@example.org");
    user.setFailedLoginAttempts(1);
    user.setLastLogin(LocalDate.of(1970, 1, 1).atStartOfDay());
    user.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    user.setPassword("iloveyou");
    user.setRoles(new HashSet<>());
    user.setUserInfo(userInfo);
    user.setUsername("janedoe");

    Country country2 = new Country();
    country2.setAbv("42");
    country2.setAbv3("42");
    country2.setAbv3_alt("42");
    country2.setCode("42");
    country2.setId(2L);
    country2.setName("42");
    country2.setSlug("42");

    UserInfo userInfo2 = new UserInfo();
    userInfo2.setAddress("17 High St");
    userInfo2.setCity("London");
    userInfo2.setCountry(country2);
    userInfo2.setFirstName("John");
    userInfo2.setGender(GenderEnum.FEMALE);
    userInfo2.setId(2L);
    userInfo2.setImageUrl("Image Url");
    userInfo2.setLastName("Smith");
    userInfo2.setPhoneNumber("8605550118");
    userInfo2.setSecondaryAddress("17 High St");
    userInfo2.setState("State");
    userInfo2.setZipCode("OX1 1PT");

    userInfoRepository.save(userInfo2);

    User user2 = new User();
    user2.setActivationCodes(new HashSet<>());
    user2.setActive(false);
    user2.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    user2.setEmail("john.smith@example.org");
    user2.setFailedLoginAttempts(0);
    user2.setLastLogin(LocalDate.of(1970, 1, 1).atStartOfDay());
    user2.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    user2.setPassword("Password");
    user2.setRoles(new HashSet<>());
    user2.setUserInfo(userInfo2);
    user2.setUsername("Username");

    userRepository.save(user);

    userRepository.save(user2);

    userRepository.findByEmailOrUsername("janedoe");
  }

  /**
   * Method under test: {@link UserRepository#findByActivationCodesIn(Set)}
   */
  @Test
  void testFindByActivationCodesIn() {
    Country country = createCountry();

    UserInfo userInfo = new UserInfo();
    userInfo.setAddress("42 Main St");
    userInfo.setCity("Oxford");
    userInfo.setCountry(country);
    userInfo.setFirstName("Jane");
    userInfo.setGender(GenderEnum.MALE);
    userInfo.setId(1L);
    userInfo.setImageUrl("https://example.org/example");
    userInfo.setLastName("Doe");
    userInfo.setPhoneNumber("6625550144");
    userInfo.setSecondaryAddress("42 Main St");
    userInfo.setState("MD");
    userInfo.setUuid(UUID.randomUUID());
    userInfo.setZipCode("21654");

    User user = new User();
    user.setActivationCodes(new HashSet<>());
    user.setActive(true);
    user.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    user.setEmail("jane.doe@example.org");
    user.setFailedLoginAttempts(1);
    user.setLastLogin(LocalDate.of(1970, 1, 1).atStartOfDay());
    user.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    user.setPassword("iloveyou");
    user.setRoles(new HashSet<>());
    user.setUserInfo(userInfo);
    user.setUsername("janedoe");
    user.setUuid(UUID.randomUUID());

    List<UserActivationCode> activationCodes = createActivationCodes();
    userActivationCodeRepository.saveAll(activationCodes);

    user.setActivationCodes(new HashSet<>(activationCodes));

    userInfoRepository.save(userInfo);

    userRepository.save(user);

    userRepository.findByActivationCodesIn(new HashSet<>(activationCodes));
  }

  private static Country createCountry() {
    Country country = new Country();
    country.setAbv("Abv");
    country.setAbv3("Abv3");
    country.setAbv3_alt("Abv3 alt");
    country.setCode("Code");
    country.setId(1L);
    country.setName("Name");
    country.setSlug("Slug");
    country.setUuid(UUID.randomUUID());

    return country;
  }

  private List<UserActivationCode> createActivationCodes() {
    return List.of(
            createActivationCode("code1")
    );
  }

  private UserActivationCode createActivationCode(String activationCode) {
    UserActivationCode userActivationCode = new UserActivationCode();

    User user = new User();
    user.setUsername("username with " + activationCode);
    user.setEmail("user@" + activationCode);
    user.setPassword("password " + activationCode);

    userRepository.save(user);

    userActivationCode.setUser(user);
    userActivationCode.setActivationCode(activationCode);

    return userActivationCode;
  }

  /**
   * Method under test: {@link UserRepository#findByUsername(String)}
   */
  @Test
  void testFindByUsername() {
    Country country = createCountry();

    UserInfo userInfo = new UserInfo();
    userInfo.setAddress("42 Main St");
    userInfo.setCity("Oxford");
    userInfo.setCountry(country);
    userInfo.setFirstName("Jane");
    userInfo.setGender(GenderEnum.MALE);
    userInfo.setId(1L);
    userInfo.setImageUrl("https://example.org/example");
    userInfo.setLastName("Doe");
    userInfo.setPhoneNumber("6625550144");
    userInfo.setSecondaryAddress("42 Main St");
    userInfo.setState("MD");
    userInfo.setUuid(UUID.randomUUID());
    userInfo.setZipCode("21654");

    User user = new User();
    user.setActivationCodes(new HashSet<>());
    user.setActive(true);
    user.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    user.setEmail("jane.doe@example.org");
    user.setFailedLoginAttempts(1);
    user.setLastLogin(LocalDate.of(1970, 1, 1).atStartOfDay());
    user.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    user.setPassword("iloveyou");
    user.setRoles(new HashSet<>());
    user.setUserInfo(userInfo);
    user.setUsername("janedoe");
    user.setUuid(UUID.randomUUID());

    Country country2 = new Country();
    country2.setAbv("42");
    country2.setAbv3("42");
    country2.setAbv3_alt("42");
    country2.setCode("42");
    country2.setId(2L);
    country2.setName("42");
    country2.setSlug("42");
    country2.setUuid(UUID.randomUUID());

    UserInfo userInfo2 = new UserInfo();
    userInfo2.setAddress("17 High St");
    userInfo2.setCity("London");
    userInfo2.setCountry(country2);
    userInfo2.setFirstName("John");
    userInfo2.setGender(GenderEnum.FEMALE);
    userInfo2.setId(2L);
    userInfo2.setImageUrl("Image Url");
    userInfo2.setLastName("Smith");
    userInfo2.setPhoneNumber("8605550118");
    userInfo2.setSecondaryAddress("17 High St");
    userInfo2.setState("State");
    userInfo2.setUuid(UUID.randomUUID());
    userInfo2.setZipCode("OX1 1PT");

    User user2 = new User();
    user2.setActivationCodes(new HashSet<>());
    user2.setActive(false);
    user2.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    user2.setEmail("john.smith@example.org");
    user2.setFailedLoginAttempts(0);
    user2.setLastLogin(LocalDate.of(1970, 1, 1).atStartOfDay());
    user2.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    user2.setPassword("Password");
    user2.setRoles(new HashSet<>());
    user2.setUserInfo(userInfo2);
    user2.setUsername("Username");
    user2.setUuid(UUID.randomUUID());

    userInfoRepository.save(userInfo);
    userRepository.save(user);

    userInfoRepository.save(userInfo2);
    userRepository.save(user2);

    userRepository.findByUsername("janedoe");
  }

  /**
   * Method under test: {@link UserRepository#deleteByUuid(UUID)}
   */
  @Test
  void testDeleteByUuid() {
    Country country = createCountry();

    UserInfo userInfo = new UserInfo();
    userInfo.setAddress("42 Main St");
    userInfo.setCity("Oxford");
    userInfo.setCountry(country);
    userInfo.setFirstName("Jane");
    userInfo.setGender(GenderEnum.MALE);
    userInfo.setId(1L);
    userInfo.setImageUrl("https://example.org/example");
    userInfo.setLastName("Doe");
    userInfo.setPhoneNumber("6625550144");
    userInfo.setSecondaryAddress("42 Main St");
    userInfo.setState("MD");
    userInfo.setUuid(UUID.randomUUID());
    userInfo.setZipCode("21654");

    User user = new User();
    user.setActivationCodes(new HashSet<>());
    user.setActive(true);
    user.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    user.setEmail("jane.doe@example.org");
    user.setFailedLoginAttempts(1);
    user.setLastLogin(LocalDate.of(1970, 1, 1).atStartOfDay());
    user.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    user.setPassword("iloveyou");
    user.setRoles(new HashSet<>());
    user.setUserInfo(userInfo);
    user.setUsername("janedoe");
    user.setUuid(UUID.randomUUID());

    Country country2 = new Country();
    country2.setAbv("42");
    country2.setAbv3("42");
    country2.setAbv3_alt("42");
    country2.setCode("42");
    country2.setId(2L);
    country2.setName("42");
    country2.setSlug("42");
    country2.setUuid(UUID.randomUUID());

    UserInfo userInfo2 = new UserInfo();
    userInfo2.setAddress("17 High St");
    userInfo2.setCity("London");
    userInfo2.setCountry(country2);
    userInfo2.setFirstName("John");
    userInfo2.setGender(GenderEnum.FEMALE);
    userInfo2.setId(2L);
    userInfo2.setImageUrl("Image Url");
    userInfo2.setLastName("Smith");
    userInfo2.setPhoneNumber("8605550118");
    userInfo2.setSecondaryAddress("17 High St");
    userInfo2.setState("State");
    userInfo2.setUuid(UUID.randomUUID());
    userInfo2.setZipCode("OX1 1PT");

    User user2 = new User();
    user2.setActivationCodes(new HashSet<>());
    user2.setActive(false);
    user2.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    user2.setEmail("john.smith@example.org");
    user2.setFailedLoginAttempts(0);
    user2.setLastLogin(LocalDate.of(1970, 1, 1).atStartOfDay());
    user2.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    user2.setPassword("Password");
    user2.setRoles(new HashSet<>());
    user2.setUserInfo(userInfo2);
    user2.setUsername("Username");
    user2.setUuid(UUID.randomUUID());

    userInfoRepository.save(userInfo);
    userRepository.save(user);

    userInfoRepository.save(userInfo2);
    userRepository.save(user2);

    userRepository.deleteByUuid(UUID.randomUUID());
  }

  /**
   * Method under test: {@link UserRepository#findByUuid(UUID)}
   */
  @Test
  void testFindByUuid() {
    Country country = createCountry();

    UserInfo userInfo = new UserInfo();
    userInfo.setAddress("42 Main St");
    userInfo.setCity("Oxford");
    userInfo.setCountry(country);
    userInfo.setFirstName("Jane");
    userInfo.setGender(GenderEnum.MALE);
    userInfo.setId(1L);
    userInfo.setImageUrl("https://example.org/example");
    userInfo.setLastName("Doe");
    userInfo.setPhoneNumber("6625550144");
    userInfo.setSecondaryAddress("42 Main St");
    userInfo.setState("MD");
    userInfo.setUuid(UUID.randomUUID());
    userInfo.setZipCode("21654");

    User user = new User();
    user.setActivationCodes(new HashSet<>());
    user.setActive(true);
    user.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    user.setEmail("jane.doe@example.org");
    user.setFailedLoginAttempts(1);
    user.setLastLogin(LocalDate.of(1970, 1, 1).atStartOfDay());
    user.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    user.setPassword("iloveyou");
    user.setRoles(new HashSet<>());
    user.setUserInfo(userInfo);
    user.setUsername("janedoe");
    user.setUuid(UUID.randomUUID());

    Country country2 = new Country();
    country2.setAbv("42");
    country2.setAbv3("42");
    country2.setAbv3_alt("42");
    country2.setCode("42");
    country2.setId(2L);
    country2.setName("42");
    country2.setSlug("42");
    country2.setUuid(UUID.randomUUID());

    UserInfo userInfo2 = new UserInfo();
    userInfo2.setAddress("17 High St");
    userInfo2.setCity("London");
    userInfo2.setCountry(country2);
    userInfo2.setFirstName("John");
    userInfo2.setGender(GenderEnum.FEMALE);
    userInfo2.setId(2L);
    userInfo2.setImageUrl("Image Url");
    userInfo2.setLastName("Smith");
    userInfo2.setPhoneNumber("8605550118");
    userInfo2.setSecondaryAddress("17 High St");
    userInfo2.setState("State");
    userInfo2.setUuid(UUID.randomUUID());
    userInfo2.setZipCode("OX1 1PT");

    User user2 = new User();
    user2.setActivationCodes(new HashSet<>());
    user2.setActive(false);
    user2.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    user2.setEmail("john.smith@example.org");
    user2.setFailedLoginAttempts(0);
    user2.setLastLogin(LocalDate.of(1970, 1, 1).atStartOfDay());
    user2.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    user2.setPassword("Password");
    user2.setRoles(new HashSet<>());
    user2.setUserInfo(userInfo2);
    user2.setUsername("Username");
    user2.setUuid(UUID.randomUUID());

    userInfoRepository.save(userInfo);
    userRepository.save(user);

    userInfoRepository.save(userInfo2);
    userRepository.save(user2);

    userRepository.findByUuid(UUID.randomUUID());
  }

}
