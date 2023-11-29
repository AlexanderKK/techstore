package com.techx7.techstore.repository;

import com.techx7.techstore.model.entity.Manufacturer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = {ManufacturerRepository.class})
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.techx7.techstore.model.entity"})
@DataJpaTest
class ManufacturerRepositoryTest {

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    private Manufacturer savedManufacturer;

    private UUID existingUuid;

    private UUID nonExistingUuid;

    @BeforeEach
    void setUp() {
        manufacturerRepository.deleteAll();

        savedManufacturer = new Manufacturer();
        savedManufacturer.setName("Test Manufacturer");
        savedManufacturer.setDescription("Test Description");
        savedManufacturer.setImageUrl("http://example.com/image.jpg");

        nonExistingUuid = UUID.randomUUID();

        savedManufacturer = manufacturerRepository.save(savedManufacturer);

        existingUuid = savedManufacturer.getUuid();
    }

    @AfterEach
    void tearDown() {
        manufacturerRepository.deleteAll();
    }

    @Test
    void testFindByNameWhenManufacturerExists() {
        // Arrange
        String name = savedManufacturer.getName();

        // Act
        Optional<Manufacturer> foundManufacturer = manufacturerRepository.findByName(name);

        // Assert
        assertThat(foundManufacturer).isPresent();
        assertThat(foundManufacturer.get().getName()).isEqualTo(name);
    }

    @Test
    void testFindByNameWhenManufacturerDoesNotExist() {
        // Arrange
        String nonExistingName = "Non Existing Name";

        // Act
        Optional<Manufacturer> foundManufacturer = manufacturerRepository.findByName(nonExistingName);

        // Assert
        assertThat(foundManufacturer).isNotPresent();
    }

    @Test
    void testDeleteByUuidWhenManufacturerExists() {
        // Arrange
        UUID uuid = existingUuid;

        // Act
        manufacturerRepository.deleteByUuid(uuid);
        Optional<Manufacturer> foundManufacturer = manufacturerRepository.findByUuid(uuid);

        // Assert
        assertThat(foundManufacturer).isNotPresent();
    }

    @Test
    void testDeleteByUuidWhenManufacturerDoesNotExist() {
        // Arrange
        UUID uuid = nonExistingUuid;

        // Act
        manufacturerRepository.deleteByUuid(uuid);
        Optional<Manufacturer> foundManufacturer = manufacturerRepository.findByUuid(uuid);

        // Assert
        assertThat(foundManufacturer).isNotPresent();
    }

    @Test
    void testFindByUuidWhenManufacturerExists() {
        // Arrange
        UUID uuid = existingUuid;

        // Act
        Optional<Manufacturer> foundManufacturer = manufacturerRepository.findByUuid(uuid);

        // Assert
        assertThat(foundManufacturer).isPresent();
        assertThat(foundManufacturer.get().getUuid()).isEqualTo(uuid);
    }

    @Test
    void testFindByUuidWhenManufacturerDoesNotExist() {
        // Arrange
        UUID uuid = nonExistingUuid;

        // Act
        Optional<Manufacturer> foundManufacturer = manufacturerRepository.findByUuid(uuid);

        // Assert
        assertThat(foundManufacturer).isNotPresent();
    }

}
