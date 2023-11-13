package com.techx7.techstore.service;

import com.techx7.techstore.model.events.UserRegisteredEvent;

public interface UserActivationService {

    void userRegistered(UserRegisteredEvent event);

}
