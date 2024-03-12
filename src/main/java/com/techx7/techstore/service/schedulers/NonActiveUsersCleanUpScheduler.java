package com.techx7.techstore.service.schedulers;

import com.techx7.techstore.service.UserActivationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NonActiveUsersCleanUpScheduler {

    private final UserActivationService userActivationService;

    @Autowired
    public NonActiveUsersCleanUpScheduler(UserActivationService userActivationService) {
        this.userActivationService = userActivationService;
    }

    @Scheduled(cron = "0 0/15 * * * ?")
    public void cleanUp() {
        userActivationService.cleanUpObsoleteNonActiveUsers(60);
    }

}
