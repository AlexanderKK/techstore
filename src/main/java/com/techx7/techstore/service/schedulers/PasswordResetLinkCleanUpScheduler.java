package com.techx7.techstore.service.schedulers;

import com.techx7.techstore.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PasswordResetLinkCleanUpScheduler {

    private final PasswordResetService passwordResetService;

    @Autowired
    public PasswordResetLinkCleanUpScheduler(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    public void cleanUp() {
        passwordResetService.cleanUpExpiredPasswordResetLinks(5);
    }

}
