package org.govt.Authentication;

import java.security.Principal;

/**
 * Custom Principal implementation that stores user ID instead of username.
 * This allows WebSocket messages to use user ID directly from the authenticated
 * principal.
 */
public class UserIdPrincipal implements Principal {

    private final String userId;
    private final String username;

    public UserIdPrincipal(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    @Override
    public String getName() {
        // CRITICAL: Return user ID, not username
        // This is what Principal.getName() will return in controllers
        return userId;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "UserIdPrincipal{userId='" + userId + "', username='" + username + "'}";
    }
}
