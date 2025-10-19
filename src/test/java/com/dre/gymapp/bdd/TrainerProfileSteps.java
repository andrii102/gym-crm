package com.dre.gymapp.bdd;

import com.dre.gymapp.dto.trainer.TrainerProfileResponse;
import com.dre.gymapp.dto.trainer.TrainerProfileUpdateRequest;
import com.dre.gymapp.service.TrainerService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TrainerProfileSteps {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TrainerService trainerService;

    private ResultActions resultActions;

    @Given("a trainer with username {string} exists")
    public void trainer_with_username_exists(String username) {
        TrainerProfileResponse fakeResponse = new TrainerProfileResponse();
        fakeResponse.setFirstName("John");
        fakeResponse.setLastName("Doe");
        fakeResponse.setActive(true);
        when(trainerService.getTrainerProfileByUsername(username)).thenReturn(fakeResponse);
    }

    @When("a user  sends a GET request to {string}")
    public void user_sends_get_request_to(String url) throws Exception {
        this.resultActions = this.mockMvc.perform(get(url).with(user("john.doe")));
    }

    @Then("the trainer profile response status should be {int}")
    public void trainer_profile_response_status_should_be(int status) throws Exception {
        this.resultActions.andExpect(status().is(status));
    }

    @And("the response body should contain the trainer's first name {string}")
    public void response_body_should_contain_trainer_first_name(String firstName) throws Exception {
        this.resultActions.andExpect(jsonPath("$.firstName").value(firstName));
    }

    @Given("the trainer service will successfully update the profile for {string}")
    public void trainer_service_will_successfully_update_profile_for(String username) {
        TrainerProfileResponse fakeResponse = new TrainerProfileResponse();
        fakeResponse.setFirstName("Updated");
        fakeResponse.setLastName("Name");
        when(trainerService.updateTrainer(eq(username), any(TrainerProfileUpdateRequest.class))).thenReturn(fakeResponse);
    }

    @When("a user sends a PUT request to {string} with valid data:")
    public void user_sends_put_request_to_with_valid_data(String url, Map<String, String> data) throws Exception {
        String json = objectMapper.writeValueAsString(data);
        this.resultActions = this.mockMvc.perform(put(url)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("john.doe")));
    }


    @Given("the trainer service is ready to update activation status for {string}")
    public void trainer_service_ready_to_update_activation_status_for(String username) {
        doNothing().when(trainerService).setTrainerActiveStatus(username, true);
    }

    @And("the trainer service activation method should be called with {string}")
    public void trainer_service_activation_method_should_be_called_with(String status) {
        verify(trainerService, times(1)).setTrainerActiveStatus(anyString(), eq(Boolean.parseBoolean(status)));
    }

}