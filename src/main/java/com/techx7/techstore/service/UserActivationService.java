package com.techx7.techstore.service;

import com.techx7.techstore.exception.UserAlreadyActivatedException;
import com.techx7.techstore.model.events.UserRegisteredEvent;

public interface UserActivationService {

    void userRegistered(UserRegisteredEvent event);

    void cleanUpObsoleteActivationLinks(Integer minutesLifetime);

    String createActivationCode(String userEmail);

    String activateUser(String activationCode) throws UserAlreadyActivatedException;

}
