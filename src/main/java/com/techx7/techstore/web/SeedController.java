package com.techx7.techstore.web;

import com.techx7.techstore.service.SeedService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/seeds")
public class SeedController {

    private final SeedService seedService;

    public SeedController(SeedService seedService) {
        this.seedService = seedService;
    }

    @PostMapping("/entities/reset")
    public String resetEntities() throws Exception {
        seedService.cleanUpDatabase();

        seedService.seedDatabase();

        return "redirect:/users";
    }

    @PostMapping("/admin/reset")
    public String resetAdmin() {
        seedService.resetAdmin();

        return "redirect:/users";
    }

}
