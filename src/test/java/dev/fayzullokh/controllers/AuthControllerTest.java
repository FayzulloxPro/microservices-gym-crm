package dev.fayzullokh.controllers;

import dev.fayzullokh.controller.AuthController;
import dev.fayzullokh.dtos.auth.RefreshTokenRequest;
import dev.fayzullokh.dtos.auth.TokenRequest;
import dev.fayzullokh.dtos.auth.TokenResponse;
import dev.fayzullokh.exceptions.NotFoundException;
import dev.fayzullokh.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateToken_Success() throws NotFoundException {
        TokenRequest tokenRequest = new TokenRequest("username", "password");
        TokenResponse expectedResponse = new TokenResponse(new Date(), new Date());

        when(authService.generateToken(any(TokenRequest.class))).thenReturn(expectedResponse);

        ResponseEntity<TokenResponse> responseEntity = authController.generateToken(tokenRequest);

        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    void refreshToken_Success() {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest("refreshToken");
        TokenResponse expectedResponse = new TokenResponse(new Date(), new Date());

        when(authService.refreshToken(any(RefreshTokenRequest.class))).thenReturn(expectedResponse);

        ResponseEntity<TokenResponse> responseEntity = authController.refreshToken(refreshTokenRequest);

        assertEquals(expectedResponse, responseEntity.getBody());
    }

}
