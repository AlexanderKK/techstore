package com.techx7.techstore.repository;

import com.techx7.techstore.model.entity.Model;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = {ModelRepository.class, TestData.class})
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.techx7.techstore.model.entity"})
@DataJpaTest
class ModelRepositoryTest {

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private TestData testData;

    private UUID existingUuid;

    @BeforeEach
    void setUp() {
        modelRepository.deleteAll();

        Model existingModel = testData.createAndSaveModel();

        existingUuid = existingModel.getUuid();
    }

    @AfterEach
    void tearDown() {
        modelRepository.deleteAll();
    }

    @Test
    void testFindByUuidWhenModelExists() {
        Optional<Model> foundModel = modelRepository.findByUuid(existingUuid);

        assertThat(foundModel).isNotEmpty();
        assertThat(foundModel.get().getUuid()).isEqualTo(existingUuid);
    }

    @Test
    void testFindByUuidWhenModelDoesNotExist() {
        UUID nonExistentUuid = UUID.randomUUID();
        Optional<Model> foundModel = modelRepository.findByUuid(nonExistentUuid);

        assertThat(foundModel).isEmpty();
    }

    @Test
    void testDeleteByUuidWhenModelExists() {
        modelRepository.deleteByUuid(existingUuid);
        Optional<Model> foundModel = modelRepository.findByUuid(existingUuid);

        assertThat(foundModel).isEmpty();
    }

    @Test
    void testDeleteByUuidWhenModelDoesNotExist() {
        UUID nonExistentUuid = UUID.randomUUID();

        modelRepository.deleteByUuid(nonExistentUuid);

        long modelCount = modelRepository.count();

        assertThat(modelCount).isEqualTo(1);
    }

}
