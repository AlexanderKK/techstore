package com.techx7.techstore.web;

import com.techx7.techstore.model.dto.manufacturer.AddManufacturerDTO;
import com.techx7.techstore.model.dto.manufacturer.ManufacturerDTO;
import com.techx7.techstore.service.ManufacturerService;
import com.techx7.techstore.util.FileUtils;
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
@RequestMapping("/manufacturers")
public class ManufacturerController {

    private final ManufacturerService manufacturerService;

    @Autowired
    public ManufacturerController(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    @GetMapping("/manage")
    public String manageManufacturer(Model model) {
        List<ManufacturerDTO> manufacturerDTOS = manufacturerService.getAllManufacturers();
        model.addAttribute("manufacturers", manufacturerDTOS);

        if(!model.containsAttribute("addManufacturerDTO")) {
            model.addAttribute("addManufacturerDTO", new AddManufacturerDTO());
        }


        return "manufacturers";
    }

    @PostMapping("/manage/add")
    public String addManufacturer(@Valid AddManufacturerDTO addManufacturerDTO,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) throws IOException {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addManufacturerDTO", addManufacturerDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addManufacturerDTO", bindingResult);

            return "redirect:/manufacturers/manage";
        }

        FileUtils.saveImageLocally(addManufacturerDTO.getImage());

        manufacturerService.createManufacturer(addManufacturerDTO);

        return "redirect:/manufacturers/manage";
    }

    @DeleteMapping("/manage/delete-all")
    public String deleteAllManufacturers() {
        manufacturerService.deleteAllManufacturers();

        return "redirect:/manufacturers/manage";
    }

    @DeleteMapping("/manage/delete/{uuid}")
    public String deleteManufacturer(@PathVariable("uuid") UUID uuid) {
        manufacturerService.deleteManufacturerByUuid(uuid);

        return "redirect:/manufacturers/manage";
    }

}
