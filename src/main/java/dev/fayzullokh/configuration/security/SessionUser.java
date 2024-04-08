package dev.fayzullokh.configuration.security;

import dev.fayzullokh.entity.User;
import dev.fayzullokh.exceptions.NotFoundException;
import dev.fayzullokh.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SessionUser {
    private final UserRepository userRepository;

    public User user() throws NotFoundException {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Object principal = authentication.getPrincipal();
        if (!Objects.isNull(principal)) {
            User byUsername = userRepository.findByUsername(principal.toString());
            if (Objects.isNull(byUsername)) {
                throw new UsernameNotFoundException("Username '%s' not found".formatted(principal.toString()));
            }
            return byUsername;
        }
        return null;
    }

    public Long id() throws NotFoundException {
        User user = user();
        if (Objects.isNull(user))
            return -1L;
        return user.getId();
    }
}
