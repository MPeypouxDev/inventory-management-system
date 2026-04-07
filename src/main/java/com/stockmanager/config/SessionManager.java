package com.stockmanager.config;

import org.springframework.stereotype.Component;

import com.stockmanager.model.User;;

@Component
public class SessionManager {
    private User currentUser;

    public User getCurrentUser() { return currentUser; }
    public void setCurrentUser(User user) { this.currentUser = user; }
    public void logout() { this.currentUser = null; }
}
