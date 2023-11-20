package com.techx7.techstore.web;

import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.exception.ManufacturerNotFoundException;
import com.techx7.techstore.model.dto.manufacturer.ManufacturerDTO;
import com.techx7.techstore.model.dto.model.AddModelDTO;
import com.techx7.techstore.model.dto.model.ModelDTO;
import com.techx7.techstore.model.dto.model.ModelWithManufacturerDTO;
import com.techx7.techstore.service.ManufacturerService;
import com.techx7.techstore.service.ModelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.techx7.techstore.constant.Paths.BINDING_RESULT_PATH;
import static com.techx7.techstore.constant.Paths.DOT;

@Controller
@RequestMapping("/models")
public class ModelController {

    private final static String flashAttributeDTO = "addModelDTO";
    private final ModelService modelService;
    private final ManufacturerService manufacturerService;

    @Autowired
    public ModelController(ModelService modelService,
                           ManufacturerService manufacturerService) {
        this.modelService = modelService;
        this.manufacturerService = manufacturerService;
    }

    @GetMapping
    public String getModels(Model model) {
        List<ManufacturerDTO> manufacturerDTOs = manufacturerService.getAllManufacturers();
        model.addAttribute("manufacturers", manufacturerDTOs);

        List<ModelWithManufacturerDTO> modelWithManufacturerDTOs = modelService.getModelsWithManufacturers();
        model.addAttribute("models", modelWithManufacturerDTOs);

        if(!model.containsAttribute(flashAttributeDTO)) {
            model.addAttribute(flashAttributeDTO, new AddModelDTO());
        }

        return "models";
    }

    @PostMapping("/add")
    public String addModel(@Valid AddModelDTO addModelDTO,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addModelDTO", addModelDTO);
            redirectAttributes.addFlashAttribute(BINDING_RESULT_PATH + DOT + flashAttributeDTO, bindingResult);

            return "redirect:/models";
        }

        modelService.createModel(addModelDTO);

        return "redirect:/models";
    }

    @GetMapping("/edit/{uuid}")
    public String getModel(Model model,
                           @PathVariable("uuid") UUID uuid) {
        List<ManufacturerDTO> manufacturerDTOs = manufacturerService.getAllManufacturers();
        model.addAttribute("manufacturers", manufacturerDTOs);

        ModelWithManufacturerDTO modelWithManufacturerDTO = modelService.getModelWithManufacturerByUuid(uuid);

        if(!model.containsAttribute("modelToEdit")) {
            model.addAttribute("modelToEdit", modelWithManufacturerDTO);
        }

        return "model-edit";
    }

    @PatchMapping("/edit")
    public String editModel(@Valid ModelWithManufacturerDTO modelWithManufacturerDTO,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("modelToEdit", modelWithManufacturerDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.modelToEdit", bindingResult);

            return "redirect:/models/edit/" + modelWithManufacturerDTO.getUuid();
        }

        modelService.editModel(modelWithManufacturerDTO);

        return "redirect:/models";
    }

    @DeleteMapping("/delete/{uuid}")
    public String deleteModel(@PathVariable("uuid") UUID uuid) {
        modelService.deleteModelByUuid(uuid);

        return "redirect:/models";
    }

    @DeleteMapping("/delete-all")
    public String deleteAllModels() {
        modelService.deleteAllModels();

        return "redirect:/models";
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public String handleModelError(EntityNotFoundException e) {
        System.out.println(e.getMessage());

        return "redirect:/models";
    }

    @ExceptionHandler(ManufacturerNotFoundException.class)
    public String handleManufacturerError(ManufacturerNotFoundException e,
                                          RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("manufacturerError", e.getMessage());

        return "redirect:/models/edit/" + e.getUuid();
    }

}
