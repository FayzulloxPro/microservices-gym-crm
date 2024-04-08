package dev.fayzullokh.controller;

import dev.fayzullokh.dtos.AppErrorDTO;
import dev.fayzullokh.dtos.auth.RefreshTokenRequest;
import dev.fayzullokh.dtos.auth.TokenRequest;
import dev.fayzullokh.dtos.auth.TokenResponse;
import dev.fayzullokh.exceptions.NotFoundException;
import dev.fayzullokh.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "This API is used for access token generation", responses = {
            @ApiResponse(responseCode = "200", description = "Access token generated", content = @Content(schema = @Schema(implementation = TokenResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = AppErrorDTO.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> generateToken(@Valid @RequestBody TokenRequest tokenRequest) throws NotFoundException {
        log.info("Generating token for request: {}", tokenRequest);
        return ResponseEntity.ok(authService.generateToken(tokenRequest));
    }

    @Operation(summary = "This API is used for generating a new access token using the refresh token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Access token generated", content = @Content(schema = @Schema(implementation = TokenResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = AppErrorDTO.class)))
            }
    )
    @PostMapping("/refresh/token")
    public ResponseEntity<TokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        log.info("Refreshing token for request: {}", refreshTokenRequest);
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        return ResponseEntity.ok(authService.logout(request));
    }
}
