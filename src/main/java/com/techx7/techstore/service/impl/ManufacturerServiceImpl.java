package com.techx7.techstore.service.impl;

import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.model.dto.manufacturer.AddManufacturerDTO;
import com.techx7.techstore.model.dto.manufacturer.ManufacturerDTO;
import com.techx7.techstore.model.dto.manufacturer.ManufacturerWithModelsDTO;
import com.techx7.techstore.model.entity.Manufacturer;
import com.techx7.techstore.model.entity.Model;
import com.techx7.techstore.repository.ManufacturerRepository;
import com.techx7.techstore.repository.ModelRepository;
import com.techx7.techstore.repository.ProductRepository;
import com.techx7.techstore.service.ManufacturerService;
import com.techx7.techstore.service.ModelService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.techx7.techstore.constant.Messages.ENTITY_NOT_FOUND;
import static com.techx7.techstore.utils.FileUtils.uploadFile;
import static com.techx7.techstore.utils.StringUtils.getClassNameLowerCase;

@Service
public class ManufacturerServiceImpl implements ManufacturerService {

    private final ModelMapper mapper;
    private final ManufacturerRepository manufacturerRepository;
    private final ModelRepository modelRepository;
    private final ProductRepository productRepository;
    private final ModelService modelService;

    @Autowired
    public ManufacturerServiceImpl(ModelMapper mapper,
                                   ManufacturerRepository manufacturerRepository,
                                   ModelRepository modelRepository,
                                   ProductRepository productRepository,
                                   ModelService modelService) {
        this.mapper = mapper;
        this.manufacturerRepository = manufacturerRepository;
        this.modelRepository = modelRepository;
        this.productRepository = productRepository;
        this.modelService = modelService;
    }

    @Override
    public void createManufacturer(AddManufacturerDTO addManufacturerDTO) throws IOException {
        Manufacturer manufacturer = mapper.map(addManufacturerDTO, Manufacturer.class);

        uploadFile(
                addManufacturerDTO.getImage(),
                getClassNameLowerCase(Manufacturer.class),
                manufacturer.getName().toLowerCase()
        );

        manufacturerRepository.save(manufacturer);
    }

    @Override
    public List<ManufacturerDTO> getAllManufacturers() {
        return manufacturerRepository.findAll(Sort.by("name"))
                .stream()
                .map(manufacturer -> mapper.map(manufacturer, ManufacturerDTO.class))
                .toList();
    }

    @Override
    public void deleteAllManufacturers() {
        modelService.deleteAllModels();

        manufacturerRepository.deleteAll();
    }

    @Override
    @Transactional
    public void deleteManufacturerByUuid(UUID uuid) {
        Manufacturer manufacturer = manufacturerRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "Manufacturer")));

        List<Model> models = modelRepository.findAllByManufacturerId(manufacturer.getId());

        for (Model model : models) {
            modelService.clearModelsFromProducts(model);
        }

        modelRepository.deleteAll(models);

        manufacturerRepository.deleteByUuid(uuid);
    }

    @Override
    public List<ManufacturerWithModelsDTO> getManufacturersWithModelsDTO() {
        return manufacturerRepository.findAll(Sort.by("name"))
                .stream()
                .map(manufacturer -> mapper.map(manufacturer, ManufacturerWithModelsDTO.class))
                .toList();
    }

    @Override
    public ManufacturerDTO getManufacturerByUuid(UUID uuid) {
        return manufacturerRepository.findByUuid(uuid)
                .map(manufacturer -> mapper.map(manufacturer, ManufacturerDTO.class))
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "Manufacturer")));
    }

    @Override
    public void editManufacturer(ManufacturerDTO manufacturerDTO) throws IOException {
        Manufacturer manufacturer = manufacturerRepository.findByUuid(manufacturerDTO.getUuid())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "Manufacturer")));

        uploadFile(
                manufacturerDTO.getImage(),
                getClassNameLowerCase(Manufacturer.class),
                manufacturer.getName().toLowerCase()
        );

        manufacturer.editManufacturer(manufacturerDTO);

        manufacturerRepository.save(manufacturer);
    }

}
