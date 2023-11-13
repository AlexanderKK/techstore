package com.techx7.techstore.service.impl;

import com.techx7.techstore.model.events.UserRegisteredEvent;
import com.techx7.techstore.service.UserActivationService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class UserActivationServiceImpl implements UserActivationService {

    @EventListener(UserRegisteredEvent.class)
    @Override
    public void userRegistered(UserRegisteredEvent event) {
        // TODO: Add activation links
        System.out.println("User with email: " + event.getUserEmail());
    }

}
