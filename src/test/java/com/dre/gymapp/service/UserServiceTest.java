package com.dre.gymapp.service;

import com.dre.gymapp.dao.UserDao;
import com.dre.gymapp.exception.NotFoundException;
import com.dre.gymapp.exception.UnauthorizedException;
import com.dre.gymapp.model.User;
import com.dre.gymapp.util.CredentialsGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserDao userDao;

    @Mock
    private CredentialsGenerator credentialsGenerator;
    
    private User testUser;
    
    @BeforeEach
    public void setUp() {
        testUser = new User("John", "Doe");
        testUser.setUsername("john.doe");
        testUser.setPassword("<PASSWORD>");
    }

    @Test
    public void authenticateUser_ShouldReturnUser_PasswordMatches() {
        when(userDao.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        User authenticatedUser = userService.authenticateUser(testUser.getUsername(), testUser.getPassword());

        assertNotNull(authenticatedUser);
        assertEquals(testUser.getUsername(), authenticatedUser.getUsername());
    }

    @Test
    public void authenticateUser_ShouldThrowException_PasswordDoesNotMatch() {
        when(userDao.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        assertThrows(UnauthorizedException.class, () -> userService.authenticateUser(testUser.getUsername(), "wrong-password"));
    }

    @Test
    public void authenticateUser_ShouldThrowException_UserNotFound() {
        when(userDao.findByUsername(testUser.getUsername())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.authenticateUser(testUser.getUsername(), testUser.getPassword()));
    }

    @Test
    public void createUser_ShouldReturnUser_UserCreated() {
        when(credentialsGenerator.generateUsername(testUser.getFirstName(), testUser.getLastName(),
                userDao.findByUsernameStartingWith(testUser.getUsername()))).thenReturn(testUser.getUsername())
                .thenReturn("john.doe");
        when(credentialsGenerator.generatePassword()).thenReturn(testUser.getPassword())
                .thenReturn("<PASSWORD>");

        User createdUser = userService.createUser(testUser.getFirstName(), testUser.getLastName());

        assertNotNull(createdUser);
        assertEquals(testUser.getFirstName(), createdUser.getFirstName());
        assertEquals(testUser.getLastName(), createdUser.getLastName());
        assertEquals(testUser.getUsername(), createdUser.getUsername());
        assertEquals(testUser.getPassword(), createdUser.getPassword());
    }

    @Test
    public void updateUser_ShouldReturnUpdatedUser_UserUpdated() {
        when(userDao.update(testUser)).thenReturn(testUser).thenReturn(testUser);

        User updatedUser = userService.updateUser(testUser);

        assertNotNull(updatedUser);
        assertEquals(testUser.getId(), updatedUser.getId());
        assertEquals(testUser.getFirstName(), updatedUser.getFirstName());
        assertEquals(testUser.getLastName(), updatedUser.getLastName());
        assertEquals(testUser.getUsername(), updatedUser.getUsername());
        assertEquals(testUser.getPassword(), updatedUser.getPassword());
    }

    @Test
    public void updateUser_ShouldThrowException_UserNotFound() {
        when(userDao.update(testUser)).thenThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(testUser));
    }

    @Test
    public void changePassword_ShouldUpdatePassword() {
        String password = testUser.getPassword();

        userService.changePassword(testUser, "newPassword");

        assertNotEquals(password, testUser.getPassword());
        verify(userDao).update(testUser);
    }

    @Test
    public void getUserById_ShouldReturnUser() {
        when(userDao.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        User user = userService.getUserById(testUser.getId());
        assertNotNull(user);
        assertEquals(testUser.getId(), user.getId());
        assertEquals(testUser.getFirstName(), user.getFirstName());
        assertEquals(testUser.getLastName(), user.getLastName());
        assertEquals(testUser.getUsername(), user.getUsername());
        assertEquals(testUser.getPassword(), user.getPassword());
    }

    @Test
    public void getUserById_ShouldThrowException_UserNotFound() {
        when(userDao.findById(testUser.getId())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.getUserById(testUser.getId()));
    }

    @Test
    public void getUserByUsername_ShouldReturnUser() {
        when(userDao.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));
        User user = userService.getUserByUsername(testUser.getUsername());
        assertNotNull(user);
        assertEquals(testUser.getId(), user.getId());
        assertEquals(testUser.getFirstName(), user.getFirstName());
        assertEquals(testUser.getLastName(), user.getLastName());
        assertEquals(testUser.getUsername(), user.getUsername());
        assertEquals(testUser.getPassword(), user.getPassword());
    }

    @Test
    public void getUserByUsername_ShouldThrowException_UserNotFound() {
        when(userDao.findByUsername(testUser.getUsername())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.getUserByUsername(testUser.getUsername()));
    }

    @Test
    public void getAllUsers_ShouldReturnAllUsers() {
        when(userDao.findAll()).thenReturn(List.of(testUser));
        List<User> users = userService.getAllUsers();
        assertNotNull(users);
        assertEquals(1, users.size());

        verify(userDao).findAll();
    }

    @Test
    public void deleteUserById_ShouldDeleteUser() {
        userService.deleteUserById(testUser.getId());
        verify(userDao).deleteById(testUser.getId());
    }

    @Test
    public void deleteUserById_ShouldThrowException_UserNotFound() {
        doThrow(NotFoundException.class).when(userDao).deleteById(testUser.getId());
        assertThrows(NotFoundException.class, () -> userService.deleteUserById(testUser.getId()));
    }

    @Test
    public void setActive_ShouldSetTrueIsActive() {
        testUser.setActive(false);
        userService.setActive(testUser, true);

        assertTrue(testUser.isActive());
        verify(userDao).update(testUser);
    }

    @Test
    public void setActive_ShouldSetFalseIsActive() {
        userService.setActive(testUser, false);

        assertFalse(testUser.isActive());
        verify(userDao).update(testUser);
    }

}
