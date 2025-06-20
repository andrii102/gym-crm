package com.dre.gymapp.util;

import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CredentialsGeneratorTest {

    CredentialsGenerator credentialsGenerator = new CredentialsGenerator();

    @Test
    public void testGenerateUsernameNotTaken() {
        String firstName = "John";
        String lastName = "Doe";
        String username = credentialsGenerator.generateUsername(firstName, lastName, s -> false);
        assertEquals("john.doe", username);
    }

    @Test
    public void testGenerateUsernameTaken() {
        String firstName = "John";
        String lastName = "Doe";
        Set<String> takenUsernames = Set.of("john.doe", "john.doe1");
        Predicate<String> isTaken = takenUsernames::contains;
        String username = credentialsGenerator.generateUsername(firstName, lastName, isTaken);
        assertEquals("john.doe2", username);
    }

    @Test
    public void testGeneratePassword() {
        String password = credentialsGenerator.generatePassword();
        assertEquals(10, password.length());
    }

}
