package com.dvt.version_tracker.web;

import com.dvt.version_tracker.model.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class SessionUser {
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean hasUser() {
        return user != null;
    }

    public void clear() {
        this.user = null;
    }
}
