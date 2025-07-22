package com.dre.gymapp.util;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class CredentialsGenerator {

    public String generateUsername(String firstName, String lastName, List<String> takenUsernames) {
        String base = firstName.toLowerCase() + "." + lastName.toLowerCase();
        int suffix = 1;
        String username = base;
        while (takenUsernames.contains(username)){
            username = base + suffix++;
        }
        return username;
    }

    public String generatePassword() {
        String chars = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm1234567890@_-/#$()!";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i < 10; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
