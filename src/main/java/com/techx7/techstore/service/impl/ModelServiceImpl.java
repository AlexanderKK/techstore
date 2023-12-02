package com.techx7.techstore.service.impl;

import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.exception.ManufacturerNotFoundException;
import com.techx7.techstore.model.dto.model.AddModelDTO;
import com.techx7.techstore.model.dto.model.ModelWithManufacturerDTO;
import com.techx7.techstore.model.entity.Manufacturer;
import com.techx7.techstore.model.entity.Model;
import com.techx7.techstore.model.entity.Product;
import com.techx7.techstore.repository.ManufacturerRepository;
import com.techx7.techstore.repository.ModelRepository;
import com.techx7.techstore.repository.ProductRepository;
import com.techx7.techstore.service.ModelService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.techx7.techstore.constant.Messages.ENTITY_NOT_FOUND;

@Service
public class ModelServiceImpl implements ModelService {

    private final ModelRepository modelRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final ModelMapper mapper;
    private final ProductRepository productRepository;

    @Autowired
    public ModelServiceImpl(ModelRepository modelRepository,
                            ManufacturerRepository manufacturerRepository,
                            ModelMapper mapper,
                            ProductRepository productRepository) {
        this.modelRepository = modelRepository;
        this.mapper = mapper;
        this.manufacturerRepository = manufacturerRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void createModel(AddModelDTO addModelDTO) {
        Model model = mapper.map(addModelDTO, Model.class);

        modelRepository.save(model);
    }

    @Override
    public List<ModelWithManufacturerDTO> getModelsWithManufacturers() {
        return modelRepository.findAll(Sort.by("manufacturerName", "name"))
                .stream()
                .map(model -> mapper.map(model, ModelWithManufacturerDTO.class))
                .toList();
    }

    @Override
    public void deleteAllModels() {
        for (Model model : modelRepository.findAll().stream().toList()) {
            clearModelsFromProducts(model);
        }

        modelRepository.deleteAll();
    }

    @Override
    @Transactional
    public void deleteModelByUuid(UUID uuid) {
        Model model = modelRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "Model")));

        clearModelsFromProducts(model);

        modelRepository.deleteByUuid(uuid);
    }

    @Override
    public void clearModelsFromProducts(Model model) {
        List<Product> products = productRepository.findAllByModelId(model.getId());

        products.forEach(product -> product.setModel(null));

        productRepository.saveAll(products);
    }

    @Override
    public ModelWithManufacturerDTO getModelWithManufacturerByUuid(UUID uuid) {
        return modelRepository.findByUuid(uuid)
                .map(model -> mapper.map(model, ModelWithManufacturerDTO.class))
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "Model")));
    }

    @Override
    public void editModel(ModelWithManufacturerDTO modelWithManufacturerDTO) {
        Model model = modelRepository.findByUuid(modelWithManufacturerDTO.getUuid())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "Model")));

        Long manufacturerId = modelWithManufacturerDTO.getManufacturerId();

        Manufacturer manufacturer = manufacturerRepository.findById(manufacturerId)
                .orElseThrow(() -> new ManufacturerNotFoundException(
                        String.format(ENTITY_NOT_FOUND, "Manufacturer"), modelWithManufacturerDTO.getUuid()));

        model.setManufacturer(manufacturer);

        model.editModel(modelWithManufacturerDTO);

        modelRepository.save(model);
    }

}
