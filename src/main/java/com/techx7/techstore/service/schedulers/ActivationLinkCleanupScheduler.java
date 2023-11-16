package com.techx7.techstore.service.schedulers;

import com.techx7.techstore.service.UserActivationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ActivationLinkCleanupScheduler {

    private final UserActivationService userActivationService;

    @Autowired
    public ActivationLinkCleanupScheduler(UserActivationService userActivationService) {
        this.userActivationService = userActivationService;
    }

    @Scheduled(cron = "0 0/1 * * * ?")
//    @Scheduled(fixedRate = 10_000, initialDelay = 10_000)
    public void cleanUp() {
        userActivationService.cleanUpObsoleteActivationLinks();
    }

}
