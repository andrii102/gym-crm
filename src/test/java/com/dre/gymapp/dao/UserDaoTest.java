package com.dre.gymapp.dao;

import com.dre.gymapp.exception.NotFoundException;
import com.dre.gymapp.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @PersistenceContext
    private EntityManager entityManager;

    // Some data from test-data.sql
    private static final Long EXISTING_USER_ID = 1L;
    private static final String EXISTING_USERNAME = "john.doe";
    private static final String NON_EXISTENT_USERNAME = "non-existent-user";

    @Test
    public void findById_ShouldReturnUser() {
        Optional<User> user = userDao.findById(EXISTING_USER_ID);

        assertTrue(user.isPresent());
        assertEquals("John", user.get().getFirstName());
        assertEquals(EXISTING_USERNAME, user.get().getUsername());
    }

    @Test
    public void findById_ShouldReturnEmptyOptional() {
        Optional<User> user = userDao.findById(9999L); // Non-existent ID
        assertFalse(user.isPresent());
    }

    @Test
    public void findByUsername_ShouldReturnUser() {
        Optional<User> user = userDao.findByUsername(EXISTING_USERNAME);

        assertTrue(user.isPresent());
        assertEquals("John", user.get().getFirstName());
        assertEquals(EXISTING_USERNAME, user.get().getUsername());
    }

    @Test
    public void findByUsername_ShouldReturnEmptyOptional() {
        Optional<User> user = userDao.findByUsername(NON_EXISTENT_USERNAME);
        assertFalse(user.isPresent());
    }

    @Test
    public void findByUsernameStartingWith_ShouldReturnUsers() {
        List<String> usernames = userDao.findByUsernameStartingWith("j");

        assertEquals(4, usernames.size());
        assertTrue(usernames.contains(EXISTING_USERNAME));
        assertTrue(usernames.contains("jane.smith"));
    }

    @Test
    public void findAll_ShouldReturnUsers() {
        List<User> users = userDao.findAll();
        assertFalse(users.isEmpty());
    }

    @Test
    public void save_ShouldPersistNewUser() {
        User newUser = new User();
        newUser.setFirstName("New");
        newUser.setLastName("User");
        newUser.setUsername("new.user");
        newUser.setPassword("<PASSWORD>");
        newUser.setActive(true);

        userDao.save(newUser);
        entityManager.flush();
        entityManager.clear();

        User persisted = entityManager.find(User.class, newUser.getId());

        assertAll(
                () -> assertNotNull(persisted),
                () -> assertEquals("New", persisted.getFirstName()),
                () -> assertEquals("new.user", persisted.getUsername())
        );
    }

    @Test
    public void update_ShouldUpdateExistingUser() {
        User user = userDao.findById(EXISTING_USER_ID).orElseThrow();

        String newFirstName = "UpdatedJohn";
        user.setFirstName(newFirstName);

        userDao.update(user);
        entityManager.flush();
        entityManager.clear();

        User dbUser = entityManager.find(User.class, EXISTING_USER_ID);
        assertEquals(newFirstName, dbUser.getFirstName());
    }

    @Test
    public void update_ShouldThrowForNewUser() {
        User newUser = new User();
        newUser.setUsername("new.user");

        assertThrows(IllegalArgumentException.class, () -> userDao.update(newUser));
    }

    @Test
    public void deleteById_ShouldRemoveUser() {
        User tempUser = new User("temp", "user");
        tempUser.setUsername("temp.user");
        tempUser.setPassword("<PASSWORD>");
        userDao.save(tempUser);
        entityManager.flush();

        userDao.deleteById(tempUser.getId());
        assertFalse(userDao.findById(tempUser.getId()).isPresent());
    }

    @Test
    public void deleteById_ShouldThrowForNonExistentUser() {
        assertThrows(NotFoundException.class, () -> userDao.deleteById(9999L));
    }
}