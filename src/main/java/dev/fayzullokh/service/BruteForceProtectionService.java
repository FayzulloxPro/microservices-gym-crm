package dev.fayzullokh.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class BruteForceProtectionService {

    private final Map<String, Integer> loginAttempts = new HashMap<>();
    private final Map<String, Long> lockedUsers = new HashMap<>();
    private final int MAX_ATTEMPTS = 3;
    private final long LOCK_DURATION = 1 * 60 * 1000; // 1 minute in milliseconds

    public void loginFailed(String username) {
        Long l = lockedUsers.get(username);
        if (!Objects.isNull(l) && l <= System.currentTimeMillis()) {
            loginAttempts.put(username, 0);
        }
        int attempts = loginAttempts.getOrDefault(username, 0) + 1;
        loginAttempts.put(username, attempts);
        if (attempts >= MAX_ATTEMPTS) {
            lockedUsers.put(username, System.currentTimeMillis() + LOCK_DURATION);
        }
    }

    public boolean isAccountLocked(String username) {
        Long unlockTime = lockedUsers.get(username);
        return unlockTime != null && unlockTime > System.currentTimeMillis();
    }

    public void loginSucceeded(String username) {
        loginAttempts.remove(username);
        lockedUsers.remove(username);
    }
}
