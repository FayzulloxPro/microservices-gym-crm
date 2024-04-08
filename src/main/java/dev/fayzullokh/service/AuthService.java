package dev.fayzullokh.service;

import dev.fayzullokh.configuration.JwtTokenBlacklist;
import dev.fayzullokh.configuration.security.JwtUtils;
import dev.fayzullokh.dtos.auth.RefreshTokenRequest;
import dev.fayzullokh.dtos.auth.TokenRequest;
import dev.fayzullokh.dtos.auth.TokenResponse;
import dev.fayzullokh.entity.User;
import dev.fayzullokh.enums.TokenType;
import dev.fayzullokh.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository authUserRepository;
    private final JwtUtils jwtTokenUtil;
    private final BruteForceProtectionService protectionService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenBlacklist tokenBlacklist;

    public TokenResponse generateToken(@NonNull TokenRequest tokenRequest) {
        String username = tokenRequest.username();
        String password = tokenRequest.password();

        log.debug("Generating token for user: {}", username);

        User byUsername = authUserRepository.findByUsername(username);
        if (Objects.isNull(byUsername)) {
            throw new UsernameNotFoundException("Username '%s' not found".formatted(username));
        }

        if (protectionService.isAccountLocked(username)) {
            throw new LockedException("Account locked, please try again later");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (AuthenticationException exception) {
            protectionService.loginFailed(username);
            throw new BadCredentialsException("Bad credentials");
        }
        protectionService.loginSucceeded(username);
        return jwtTokenUtil.generateToken(username);
    }

    public TokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.refreshToken();

        log.debug("Refreshing token");

        if (!jwtTokenUtil.isValid(refreshToken, TokenType.REFRESH)) {
            log.error("Invalid refresh token");
            throw new CredentialsExpiredException("Token is invalid");
        }

        String username = jwtTokenUtil.getUsername(refreshToken, TokenType.REFRESH);
        User byUsername = authUserRepository.findByUsername(username);
        if (Objects.isNull(byUsername)) {
            throw new UsernameNotFoundException("Username '%s' not found".formatted(username));
        }

        TokenResponse tokenResponse = TokenResponse.builder()
                .refreshToken(refreshToken)
                .refreshTokenExpiry(jwtTokenUtil.getExpiry(refreshToken, TokenType.REFRESH))
                .build();

        return jwtTokenUtil.generateAccessToken(username, tokenResponse);
    }

    public String logout(HttpServletRequest request) {
        String token = resolveToken(request);
        if (token != null) {
            tokenBlacklist.invalidateToken(token);
            return "Logout successful";
        }
        return "Invalid token";
    }

    private String resolveToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (Objects.isNull(authHeader) || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring(7);
    }
}
