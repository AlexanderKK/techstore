package com.techx7.techstore.web;

import com.techx7.techstore.model.dto.manufacturer.ManufacturerHomeDTO;
import com.techx7.techstore.model.dto.model.ModelsWithManufacturersDTO;
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

@Controller
@RequestMapping("/models")
public class ModelController {

    private final ModelService modelService;
    private final ManufacturerService manufacturerService;

    @Autowired
    public ModelController(ModelService modelService,
                           ManufacturerService manufacturerService) {
        this.modelService = modelService;
        this.manufacturerService = manufacturerService;
    }

    @GetMapping("/manage")
    public String manageModel(Model model) {
        List<ManufacturerHomeDTO> manufacturerHomeDTOs = manufacturerService.getAllManufacturers();
        model.addAttribute("manufacturers", manufacturerHomeDTOs);

        List<ModelsWithManufacturersDTO> modelsWithManufacturersDTOs = modelService.getModelsWithManufacturers();
        model.addAttribute("models", modelsWithManufacturersDTOs);

        if(!model.containsAttribute("addModelDTO")) {
            model.addAttribute("addModelDTO", new AddModelDTO());
        }

        return "models";
    }

    @PostMapping("/manage/add")
    public String addModel(@Valid AddModelDTO addModelDTO,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes) throws IOException {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addModelDTO", addModelDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addModelDTO", bindingResult);

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
