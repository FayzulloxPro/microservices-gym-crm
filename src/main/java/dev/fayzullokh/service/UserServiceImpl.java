package dev.fayzullokh.service;

import dev.fayzullokh.configuration.security.SessionUser;
import dev.fayzullokh.dtos.ChangePasswordRequest;
import dev.fayzullokh.entity.User;
import dev.fayzullokh.exceptions.DuplicateUsernameException;
import dev.fayzullokh.exceptions.NotFoundException;
import dev.fayzullokh.exceptions.UnknownException;
import dev.fayzullokh.repositories.UserRepository;
import dev.fayzullokh.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SessionUser sessionUser;

    @Override
    public User createUser(User user) throws UnknownException, DuplicateUsernameException {
        log.debug("Creating user: {}", user);
        user.setUsername(generateUserName(user.getFirstName(), user.getLastName()));
        user.setActive(true);
//        String generatePassword = Utils.generatePassword();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            userRepository.save(user);
        } catch (Exception e) {
            if (e.getMessage().equals("duplicate key value violates")) {
                throw new DuplicateUsernameException("User already exists with '%s' username".formatted(user.getUsername()));
            }
            throw new UnknownException("Something went wrong");
        }
        log.info("User created successfully: {}", user);
        return user;
    }

    private String generateUserName(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;

        // Use Hibernate query to check for existing usernames
        boolean usernameExists = userRepository.existsByUsername(baseUsername);

        // Return the baseUsername if it doesn't exist, otherwise append a suffix
        String generatedUsername = usernameExists ? baseUsername + Utils.getUserNameSuffix() : baseUsername;

        log.info("Generated username: {}", generatedUsername);
        return generatedUsername;
    }

    @Override
    public User getUserById(Long id) throws NotFoundException {
        log.debug("Getting user by ID: {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found with ID: " + id));
        log.info("User retrieved successfully: {}", user);
        return user;
    }

    @Override
    public List<User> findAllUsers() {
        log.debug("Getting all users");
        List<User> users = userRepository.findAll();
        log.info("Total users found: {}", users.size());
        return users;
    }

    @Override
    public User updateUser(User user) {
        log.debug("Updating user: {}", user);
        User updatedUser = userRepository.save(user);
        log.info("User updated successfully: {}", updatedUser);
        return updatedUser;
    }

    @Override
    public String changePassword(ChangePasswordRequest request) throws NotFoundException {
        User user = sessionUser.user();
        log.debug("Changing password for user: {}", user);

        if (!user.getPassword().equals(request.getOldPassword()) || !user.getUsername().equals(request.getUsername())) {
            throw new BadCredentialsException("Wrong password");
        }
        user.setPassword(request.getNewPassword());
        userRepository.save(user);
        log.info("Password changed successfully for user: {}", user);
        return "New password set successfully";
    }

    @Override
    public String changeActivation(boolean status) throws NotFoundException {
        Long id = sessionUser.id();
        log.debug("Changing activation status for user ID: {}", id);
        User byId = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found with ID: " + id));
        byId.setActive(status);
        userRepository.save(byId);
        log.info("Activation status changed successfully for user: {}", byId);
        return (status) ? "User activated successfully" : "User deactivated successfully";
    }
}
