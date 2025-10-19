package com.dre.gymapp.bdd;

import com.dre.gymapp.security.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AuthSteps {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected CustomUserDetailsService userDetailsService;
    @Autowired
    protected PasswordEncoder passwordEncoder;
    @Autowired
    protected StringRedisTemplate stringRedisTemplate;

    private ValueOperations<String, String> valueOperationsMock;

    private ResultActions resultActions;

    @Given("a registered user {string} exists with password {string}")
    public void registered_user_exists(String username, String rawPassword) {
        String hashedPassword = "hashed-" + rawPassword;
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        UserDetails userDetails = new User(username, hashedPassword, authorities);

        this.valueOperationsMock = mock(ValueOperations.class);
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperationsMock);

        // This line helps LoginAttemptService (isBlocked returns false)
        when(stringRedisTemplate.hasKey(anyString())).thenReturn(false);

        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(passwordEncoder.matches(eq(rawPassword), eq(hashedPassword))).thenReturn(true);
        when(passwordEncoder.matches(argThat(pass -> !pass.equals(rawPassword)), eq(hashedPassword))).thenReturn(false);
    }

    @Given("no user with username {string} exists")
    public void no_user_with_username_exists(String username) {
        this.valueOperationsMock = mock(ValueOperations.class);
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperationsMock);
        when(stringRedisTemplate.hasKey(anyString())).thenReturn(false);

        when(userDetailsService.loadUserByUsername(username))
                .thenThrow(new UsernameNotFoundException("User not found"));
    }

    @When("the user sends a POST request to {string} with username {string} and password {string}")
    public void the_user_sends_post_request(String url, String username, String password) throws Exception {
        Map<String, String> loginRequest = Map.of("username", username, "password", password);

        this.resultActions = this.mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
                .with(csrf()));
    }

    @Then("the server response status should be {int}")
    public void response_status_should_be(int statusCode) throws Exception {
        resultActions.andExpect(status().is(statusCode));
    }

    @And("the response body should contain a JWT token")
    public void response_body_should_contain_jwt_token() throws Exception {
        resultActions.andExpect(jsonPath("$.accessToken").exists());
    }

    @And("the refresh token should be in the cookie")
    public void refresh_token_should_be_in_cookie() throws Exception {
        resultActions.andExpect(cookie().exists("refresh_token"));
    }
}