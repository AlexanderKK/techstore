package com.techx7.techstore.web;

import com.techx7.techstore.model.dto.manufacturer.AddManufacturerDTO;
import com.techx7.techstore.model.dto.manufacturer.ManufacturerDTO;
import com.techx7.techstore.service.ManufacturerService;
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
@RequestMapping("/manufacturers")
public class ManufacturerController {

    private final static String flashAttributeDTO = "addManufacturerDTO";
    private final ManufacturerService manufacturerService;

    @Autowired
    public ManufacturerController(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    @GetMapping
    public String manageManufacturer(Model model) {
        List<ManufacturerDTO> manufacturerDTOS = manufacturerService.getAllManufacturers();
        model.addAttribute("manufacturers", manufacturerDTOS);

        if(!model.containsAttribute(flashAttributeDTO)) {
            model.addAttribute(flashAttributeDTO, new AddManufacturerDTO());
        }

        return "manufacturers";
    }

    @PostMapping("/add")
    public String addManufacturer(@Valid AddManufacturerDTO addManufacturerDTO,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) throws IOException {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(flashAttributeDTO, addManufacturerDTO);
            redirectAttributes.addFlashAttribute(BINDING_RESULT_PATH + DOT + flashAttributeDTO, bindingResult);

            return "redirect:/manufacturers";
        }

        manufacturerService.createManufacturer(addManufacturerDTO);

        return "redirect:/manufacturers";
    }

    @GetMapping("/edit/{uuid}")
    public String getManufacturer(Model model,
                              @PathVariable("uuid") UUID uuid) {
        ManufacturerDTO manufacturerDTO = manufacturerService.getManufacturerByUuid(uuid);

        if(!model.containsAttribute("manufacturerToEdit")) {
            model.addAttribute("manufacturerToEdit", manufacturerDTO);
        }

        return "manufacturer-edit";
    }

    @PatchMapping("/edit")
    public String editManufacturer(@Valid ManufacturerDTO manufacturerDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) throws IOException {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("manufacturerToEdit", manufacturerDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.manufacturerToEdit", bindingResult);

            return "redirect:/manufacturers/edit/" + manufacturerDTO.getUuid();
        }

        manufacturerService.editManufacturer(manufacturerDTO);

        return "redirect:/manufacturers";
    }

    @DeleteMapping("/delete/{uuid}")
    public String deleteManufacturer(@PathVariable("uuid") UUID uuid) {
        manufacturerService.deleteManufacturerByUuid(uuid);

        return "redirect:/manufacturers";
    }

    @DeleteMapping("/delete-all")
    public String deleteAllManufacturers() {
        manufacturerService.deleteAllManufacturers();

        return "redirect:/manufacturers";
    }

}
