package com.dre.gymapp.util;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CredentialsGeneratorTest {

    CredentialsGenerator credentialsGenerator = new CredentialsGenerator();

    @Test
    public void testGenerateUsernameNotTaken() {
        String firstName = "John";
        String lastName = "Doe";
        String username = credentialsGenerator.generateUsername(firstName, lastName, new ArrayList<>());
        assertEquals("john.doe", username);
    }

    @Test
    public void testGenerateUsernameTaken() {
        String firstName = "John";
        String lastName = "Doe";
        List<String> takenUsernames = List.of("john.doe", "john.doe1");
        String username = credentialsGenerator.generateUsername(firstName, lastName, takenUsernames);
        assertEquals("john.doe2", username);
    }

    @Test
    public void testGeneratePassword() {
        String password = credentialsGenerator.generatePassword();
        assertEquals(10, password.length());
    }

}
