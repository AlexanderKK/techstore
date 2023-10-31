package com.techx7.techstore.web;

import com.techx7.techstore.model.dto.manufacturer.AddManufacturerDTO;
import com.techx7.techstore.model.dto.manufacturer.ManufacturerHomeDTO;
import com.techx7.techstore.service.ManufacturerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.techx7.techstore.constant.FilePaths.RESOURCES_IMAGES_DIRECTORY;

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
        List<ManufacturerHomeDTO> manufacturerHomeDTOs = manufacturerService.getAllManufacturers();
        model.addAttribute("manufacturers", manufacturerHomeDTOs);

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

        saveImageLocally(addManufacturerDTO.getImage());

        manufacturerService.createManufacturer(addManufacturerDTO);

        return "redirect:/manufacturers/manage";
    }

    private void saveImageLocally(MultipartFile image) throws IOException {
        byte[] imgBytes = image.getBytes();

        if(imgBytes.length != 0) {
//            String imgDir = Objects.requireNonNull(
//                    getClass().getResource("/static/images/")).toURI().getPath();

            BufferedOutputStream bufferedWriter = new BufferedOutputStream(
                    new FileOutputStream(RESOURCES_IMAGES_DIRECTORY + image.getOriginalFilename()));

            bufferedWriter.write(imgBytes);
            bufferedWriter.close();
        }
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
