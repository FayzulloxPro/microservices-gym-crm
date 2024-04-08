package dev.fayzullokh.services;

import dev.fayzullokh.configuration.security.SessionUser;
import dev.fayzullokh.dtos.ChangePasswordRequest;
import dev.fayzullokh.entity.User;
import dev.fayzullokh.exceptions.DuplicateUsernameException;
import dev.fayzullokh.exceptions.NotFoundException;
import dev.fayzullokh.exceptions.UnknownException;
import dev.fayzullokh.repositories.UserRepository;
import dev.fayzullokh.service.UserServiceImpl;
import dev.fayzullokh.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SessionUser sessionUser;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser_Success() throws UnknownException, DuplicateUsernameException {
        User user = new User();
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.createUser(user);

        assertNotNull(result);
        assertEquals(user, result);
        assertNotNull(result.getUsername());
        assertNotNull(result.getPassword());

        verify(passwordEncoder, times(1)).encode(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testGetUserById_Success() throws NotFoundException {
        long userId = 1L;
        User user = new User();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        User result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals(user, result);

        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetUserById_Failure_NotFound() {
        long nonExistentUserId = 99L;
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUserById(nonExistentUserId));

        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void testUpdateUser_Success() {
        User user = new User();
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.updateUser(user);

        assertNotNull(result);
        assertEquals(user, result);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testChangePassword_Success() throws NotFoundException {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("oldPassword");
        when(sessionUser.user()).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        ChangePasswordRequest request = new ChangePasswordRequest("testUser", "oldPassword", "newPassword");

        String result = userService.changePassword(request);

        assertEquals("New password set successfully", result);

        verify(sessionUser, times(1)).user();
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testChangePassword_Failure_WrongPassword() throws NotFoundException {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("oldPassword");
        when(sessionUser.user()).thenReturn(user);

        ChangePasswordRequest request = new ChangePasswordRequest("testUser", "wrongPassword", "newPassword");

        assertThrows(BadCredentialsException.class, () -> userService.changePassword(request));

        verify(sessionUser, times(1)).user();
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testChangeActivation_Success() throws NotFoundException {
        User user = new User();
        user.setId(1L);
        user.setActive(true);
        when(sessionUser.id()).thenReturn(1L);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        String result = userService.changeActivation(false);

        assertEquals("User deactivated successfully", result);

        verify(sessionUser, times(1)).id();
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testChangeActivation_Failure_UserNotFound() throws NotFoundException {
        when(sessionUser.id()).thenReturn(1L);
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.changeActivation(false));

        verify(sessionUser, times(1)).id();
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, never()).save(any(User.class));
    }
}
