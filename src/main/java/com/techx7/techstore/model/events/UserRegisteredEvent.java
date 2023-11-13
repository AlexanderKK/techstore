package com.techx7.techstore.model.events;

import org.springframework.context.ApplicationEvent;

public class UserRegisteredEvent extends ApplicationEvent {

    private final String userEmail;
    private final String userName;

    public UserRegisteredEvent(Object source,
                               String userEmail,
                               String userName) {
        super(source);
        this.userEmail = userEmail;
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserName() {
        return userName;
    }

}
