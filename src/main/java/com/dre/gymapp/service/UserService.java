package com.dre.gymapp.service;

import com.dre.gymapp.dao.UserDao;
import com.dre.gymapp.dto.user.GeneratedUser;
import com.dre.gymapp.exception.NotFoundException;
import com.dre.gymapp.model.User;
import com.dre.gymapp.util.CredentialsGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private UserDao userDao;
    private CredentialsGenerator credentialsGenerator;
    private PasswordEncoder passwordEncoder;

    // Setter injection for UserDao
    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    // Setter injection for CredentialsGenerator
    @Autowired
    public void setCredentialsGenerator(CredentialsGenerator credentialsGenerator) {
        this.credentialsGenerator = credentialsGenerator;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    // Creates a new user with the given first and last name
    public GeneratedUser createUser(String firstName, String lastName) {
        logger.info("Creating new user");
        User user = new User(firstName, lastName);

        String username = credentialsGenerator.generateUsername(firstName, lastName,
                userDao.findByUsernameStartingWith(firstName.toLowerCase() + "." + lastName.toLowerCase()));
        String password = credentialsGenerator.generatePassword();
        String hashedPassword = passwordEncoder.encode(password);

        user.setUsername(username);
        user.setPassword(hashedPassword);

        userDao.save(user);
        logger.info("User created successfully");
        return new GeneratedUser(user, password);
    }

    // Updates an existing user
    public User updateUser(User user) {
        logger.info("Updating user with ID: {}", user.getId());
        try {
            return userDao.update(user);
        } catch (IllegalArgumentException e) {
            logger.warn("User cannot have null ID: {}", e.getMessage());
            throw e;
        }
    }

    // Changes user's password
    public void changePassword(String username, String newPassword) {
        logger.info("Changing password for user with username: {}", username);
        User user = getUserByUsername(username);
        user.setPassword(passwordEncoder.encode(newPassword));
        userDao.update(user);
    }

    // Gets a user by their ID
    @Deprecated // method is used only in tests
    public User getUserById(Long id) {
        logger.info("Getting user with ID: {}", id);
        try {
            return userDao.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        } catch (NotFoundException e) {
            logger.warn("User with ID {} not found: {}", id, e.getMessage());
            throw e;
        }
    }

    // Gets a user by their username
    public User getUserByUsername(String username) {
        logger.info("Getting user with username: {}", username);
        try {
            return userDao.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));
        } catch (NotFoundException e) {
            logger.warn("User with username {} not found: {}", username, e.getMessage());
            throw e;
        }
    }

    // Gets all users in the system
    @Deprecated // method is used only in tests
    public List<User> getAllUsers() {
        logger.info("Getting all users");
        return userDao.findAll();
    }

    // Deletes user by id
    @Deprecated // method is used only in tests
    public void deleteUserById(Long id) {
        logger.info("Deleting user with ID: {}", id);
        try {
            userDao.deleteById(id);
        } catch (NotFoundException e) {
            logger.warn("User with ID {} not found: {}", id, e.getMessage());
            throw e;
        }
    }

    // Activates/De-activates user
    public void setActive(User user, boolean active) {
        user.setActive(active);
        userDao.update(user);
    }
}