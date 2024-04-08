package dev.fayzullokh.config;

import dev.fayzullokh.configuration.JwtTokenBlacklist;
import dev.fayzullokh.configuration.security.JWTAuthenticationFilter;
import dev.fayzullokh.configuration.security.JwtUtils;
import dev.fayzullokh.enums.TokenType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.io.IOException;

import static org.mockito.Mockito.*;

class JWTAuthenticationFilterTest {

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private JWTAuthenticationFilter jwtAuthenticationFilter;
    private JwtTokenBlacklist jwtTokenBlacklist;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtAuthenticationFilter = new JWTAuthenticationFilter(jwtUtils, userDetailsService, jwtTokenBlacklist);
    }

    @Test
    void doFilterInternal_AuthorizationHeaderNull() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_TokenInvalid() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Bearer invalidToken");
        when(jwtUtils.getUsername("invalidToken", TokenType.ACCESS)).thenReturn(null);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_UserNotFound() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtUtils.getUsername("validToken", TokenType.ACCESS)).thenReturn("testUser");
        when(userDetailsService.loadUserByUsername("testUser")).thenReturn(new dev.fayzullokh.entity.User());

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_Success() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtUtils.getUsername("validToken", TokenType.ACCESS)).thenReturn("testUser");
        UserDetails userDetails = User.withUsername("testUser").password("password").roles("USER").build();
        when(userDetailsService.loadUserByUsername("testUser")).thenReturn(userDetails);
        when(jwtUtils.isTokenValid("validToken", TokenType.ACCESS)).thenReturn(true);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verify(request).getHeader("Authorization");
        verify(jwtUtils).getUsername("validToken", TokenType.ACCESS);
        verify(userDetailsService).loadUserByUsername("testUser");
    }
}
