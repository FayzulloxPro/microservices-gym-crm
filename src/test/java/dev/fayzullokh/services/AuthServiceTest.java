package dev.fayzullokh.services;

import dev.fayzullokh.configuration.security.JwtUtils;
import dev.fayzullokh.dtos.auth.RefreshTokenRequest;
import dev.fayzullokh.dtos.auth.TokenRequest;
import dev.fayzullokh.dtos.auth.TokenResponse;
import dev.fayzullokh.entity.User;
import dev.fayzullokh.enums.TokenType;
import dev.fayzullokh.repositories.UserRepository;
import dev.fayzullokh.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Mock
    private UserRepository authUserRepository;

    @Mock
    private JwtUtils jwtTokenUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /*@Test
    void testGenerateToken_ValidCredentials_ReturnsTokenResponse() throws NotFoundException {
        TokenRequest tokenRequest = new TokenRequest("validUsername", "validPassword");
        User user = new User();
        user.setUsername("validUsername");
        user.setPassword("validPassword");
        when(authUserRepository.findByUsername("validUsername")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("validPassword", "validPassword")).thenReturn(true);
        when(jwtTokenUtil.generateToken("validUsername")).thenReturn(new TokenResponse());

        TokenResponse result = authService.generateToken(tokenRequest);

        assertNotNull(result);
        verify(authUserRepository, times(1)).findByUsername(anyString());
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
        verify(jwtTokenUtil, times(1)).generateToken(anyString());
    }*/


    @Test
    void testGenerateToken_InvalidCredentials_ThrowsBadCredentialsException() {
        TokenRequest tokenRequest = new TokenRequest("invalidUsername", "invalidPassword");
        when(authUserRepository.findByUsername(anyString())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> authService.generateToken(tokenRequest));
        verify(authUserRepository, times(1)).findByUsername(anyString());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtTokenUtil, never()).generateToken(anyString());
    }

    @Test
    void testRefreshToken_ValidRefreshToken_ReturnsTokenResponse() {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest("validRefreshToken");
        when(jwtTokenUtil.isValid(anyString(), eq(TokenType.REFRESH))).thenReturn(true);
        when(jwtTokenUtil.getUsername(anyString(), eq(TokenType.REFRESH))).thenReturn("validUsername");
        when(authUserRepository.findByUsername(anyString())).thenReturn(new User());
        when(jwtTokenUtil.generateAccessToken(anyString(), any(TokenResponse.class))).thenReturn(new TokenResponse());

        TokenResponse result = authService.refreshToken(refreshTokenRequest);

        assertNotNull(result);
        verify(jwtTokenUtil, times(1)).isValid(anyString(), eq(TokenType.REFRESH));
        verify(jwtTokenUtil, times(1)).getUsername(anyString(), eq(TokenType.REFRESH));
        verify(authUserRepository, times(1)).findByUsername(anyString());
        verify(jwtTokenUtil, times(1)).generateAccessToken(anyString(), any(TokenResponse.class));
    }

    @Test
    void testRefreshToken_InvalidRefreshToken_ThrowsCredentialsExpiredException() {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest("invalidRefreshToken");
        when(jwtTokenUtil.isValid(anyString(), eq(TokenType.REFRESH))).thenReturn(false);

        assertThrows(CredentialsExpiredException.class, () -> authService.refreshToken(refreshTokenRequest));
        verify(jwtTokenUtil, times(1)).isValid(anyString(), eq(TokenType.REFRESH));
        verify(authUserRepository, never()).findByUsername(anyString());
        verify(jwtTokenUtil, never()).getUsername(anyString(), eq(TokenType.REFRESH));
        verify(jwtTokenUtil, never()).generateAccessToken(anyString(), any(TokenResponse.class));
    }
}
