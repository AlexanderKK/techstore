package com.techx7.techstore.repository;

import com.techx7.techstore.model.entity.Country;
import com.techx7.techstore.testUtils.TestData;
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

@ContextConfiguration(classes = {CountryRepository.class, TestData.class})
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.techx7.techstore.model.entity"})
@DataJpaTest
class CountryRepositoryTest {

    @Autowired
    private CountryRepository countryRepository;

    private String existingName;

    @BeforeEach
    void setUp() {
        countryRepository.deleteAll();

        Country existingCountry = new Country();

        existingCountry.setName("Test Country");
        existingCountry.setAbv("TC");
        existingCountry.setAbv3("TC3");
        existingCountry.setAbv3_alt("TC3");
        existingCountry.setCode("123");
        existingCountry.setSlug("test-country");

        countryRepository.save(existingCountry);

        existingName = existingCountry.getName();
    }

    @AfterEach
    void tearDown() {
        countryRepository.deleteAll();
    }

    @Test
    void testFindByNameWhenCountryExists() {
        Optional<Country> foundCountry = countryRepository.findByName(existingName);

        assertThat(foundCountry).isNotEmpty();
        assertThat(foundCountry.get().getName()).isEqualTo(existingName);
    }

    @Test
    void testFindByNameWhenCountryDoesNotExist() {
        Optional<Country> foundCountry = countryRepository.findByName("Nonexistent Country");

        assertThat(foundCountry).isEmpty();
    }

}
