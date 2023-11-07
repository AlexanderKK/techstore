package com.techx7.techstore.web;

import com.techx7.techstore.exception.ManufacturerNotFoundException;
import com.techx7.techstore.exception.ModelNotFoundException;
import com.techx7.techstore.model.dto.manufacturer.ManufacturerDTO;
import com.techx7.techstore.model.dto.model.ModelWithManufacturerDTO;
import com.techx7.techstore.model.dto.model.AddModelDTO;
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

    @ExceptionHandler(ManufacturerNotFoundException.class)
    public String handleModelError(ManufacturerNotFoundException e) {
        System.out.println(e.getMessage());

        return "redirect:/models/manage";
    }

    @GetMapping("/manage")
    public String manageModel(Model model) {
        List<ManufacturerDTO> manufacturerDTOS = manufacturerService.getAllManufacturers();
        model.addAttribute("manufacturers", manufacturerDTOS);

        List<ModelWithManufacturerDTO> modelWithManufacturerDTOs = modelService.getModelsWithManufacturers();
        model.addAttribute("models", modelWithManufacturerDTOs);

        if(!model.containsAttribute(flashAttributeDTO)) {
            model.addAttribute(flashAttributeDTO, new AddModelDTO());
        }

        return "models";
    }

    @PostMapping("/manage/add")
    public String addModel(@Valid AddModelDTO addModelDTO,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes) throws IOException {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addModelDTO", addModelDTO);
            redirectAttributes.addFlashAttribute(BINDING_RESULT_PATH + DOT + flashAttributeDTO, bindingResult);

            return "redirect:/models/manage";
        }

        modelService.createModel(addModelDTO);

        return "redirect:/models/manage";
    }

    @DeleteMapping("/manage/delete-all")
    public String deleteAllModels() {
        modelService.deleteAllModels();

        return "redirect:/models/manage";
    }

    @DeleteMapping("/manage/delete/{uuid}")
    public String deleteModel(@PathVariable("uuid") UUID uuid) {
        modelService.deleteModelByUuid(uuid);

        return "redirect:/models/manage";
    }

}
