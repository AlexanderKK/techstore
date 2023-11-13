package com.techx7.techstore.service.impl;

import com.techx7.techstore.model.events.UserRegisteredEvent;
import com.techx7.techstore.service.EmailService;
import com.techx7.techstore.service.UserActivationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class UserActivationServiceImpl implements UserActivationService {

    private final EmailService emailService;

    @Autowired
    public UserActivationServiceImpl(EmailService emailService) {
        this.emailService = emailService;
    }

    @EventListener(UserRegisteredEvent.class)
    @Override
    public void userRegistered(UserRegisteredEvent event) {
        // TODO: Add activation links
        emailService.sendRegistrationEmail(event.getUserEmail(), event.getUserName());
    }

}
