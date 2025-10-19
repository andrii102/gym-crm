package com.dre.gymapp.bdd;

import com.dre.gymapp.dto.auth.RegistrationResponse;
import com.dre.gymapp.dto.auth.TraineeRegistrationRequest;
import com.dre.gymapp.dto.auth.TrainerRegistrationRequest;
import com.dre.gymapp.dto.user.GeneratedUser;
import com.dre.gymapp.model.User;
import com.dre.gymapp.service.TraineeService;
import com.dre.gymapp.service.TrainerService;
import com.dre.gymapp.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthRegistrationSteps {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected TraineeService traineeService;

    @Autowired
    protected TrainerService trainerService;

    @Autowired
    protected UserService userService;

    private ResultActions resultActions;

    @Given("the trainee service is ready to register a new user")
    public void trainee_service_is_ready() {
        RegistrationResponse fakeResponse = new RegistrationResponse("new.trainee", "pass123");

        doReturn(fakeResponse).when(traineeService).createTrainee(any(TraineeRegistrationRequest.class));

        User fakeUser = new User("Test", "User");
        GeneratedUser fakeGeneratedUser = new GeneratedUser(fakeUser, "pass123");
        when(userService.createUser(anyString(), anyString())).thenReturn(fakeGeneratedUser);
    }

    @Given("the trainer service is ready to register a new user")
    public void trainer_service_is_ready() {
        RegistrationResponse fakeResponse = new RegistrationResponse("new.trainer", "pass456");

        doReturn(fakeResponse).when(trainerService).createTrainer(any(TrainerRegistrationRequest.class));

        User fakeUser = new User("Test", "User");
        GeneratedUser fakeGeneratedUser = new GeneratedUser(fakeUser, "pass123");
        when(userService.createUser(anyString(), anyString())).thenReturn(fakeGeneratedUser);
    }


    @When("a user sends a POST request to {string} with:")
    public void user_sends_post_request_to(String url, Map<String, String> data) throws Exception {
        String jsonBody = objectMapper.writeValueAsString(data);

        this.resultActions = this.mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody));
    }

    @Then("the server response status of registration should be {int}")
    public void server_response_status_should_be(int statusCode) throws Exception {
        System.out.println(resultActions.andReturn().getResponse().getContentAsString());
        resultActions.andExpect(status().is(statusCode));
    }

    @And("the response body should contain the new username and password")
    public void response_body_should_contain_username_and_password() throws Exception {
        resultActions.andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.password").exists());
    }
}