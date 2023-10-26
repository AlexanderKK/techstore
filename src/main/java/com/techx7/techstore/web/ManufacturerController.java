package com.techx7.techstore.web;

import com.techx7.techstore.model.dto.AddManufacturerDTO;
import com.techx7.techstore.service.ManufacturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/manufacturers")
public class ManufacturerController {

    private final ManufacturerService manufacturerService;

    @Autowired
    public ManufacturerController(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    @GetMapping("/manage/add")
    public String addManufacturer(Model model) {
        if(!model.containsAttribute("addManufacturerDTO")) {
            model.addAttribute("addManufacturerDTO", new AddManufacturerDTO());
        }

        return "manufacturer-management";
    }

    @PostMapping("/manage/add")
    public String manageManufacturer(AddManufacturerDTO addManufacturerDTO,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addManufacturerDTO", addManufacturerDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addManufacturerDTO", bindingResult);

            return "redirect:manage:add";
        }

        manufacturerService.createManufacturer(addManufacturerDTO);

        return "redirect:manage:add";
    }

}
