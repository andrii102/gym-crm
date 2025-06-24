package com.dre.gymapp.service;

import com.dre.gymapp.dao.UserDao;
import com.dre.gymapp.model.User;
import com.dre.gymapp.util.CredentialsGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private UserDao userDao;
    private CredentialsGenerator credentialsGenerator;

    @Autowired
    public void setUserDao(UserDao userDao){
        this.userDao = userDao;
    }

    @Autowired
    public void setCredentialsGenerator(CredentialsGenerator credentialsGenerator) {
        this.credentialsGenerator = credentialsGenerator;
    }

    public User createUser(String firstName, String lastName) {
        logger.info("Creating new user");
        User user = new User(firstName, lastName);

        String username = credentialsGenerator.generateUsername(firstName, lastName, userDao.findByUsernameStartingWith(firstName + "." + lastName));
        String password = credentialsGenerator.generatePassword();

        user.setUsername(username);
        user.setPassword(password);

        userDao.save(user);
        logger.info("User created successfully");
        return user;
    }

    public User getUserById(Long id) {
        logger.info("Getting user with ID: {}", id);
        try {
            return userDao.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        } catch (IllegalArgumentException e) {
            logger.warn("User with ID {} not found: {}", id, e.getMessage());
            throw e;
        }
    }

    public List<User> getAllUsers() {
        logger.info("Getting all users");
        return userDao.findAll();
    }
}
