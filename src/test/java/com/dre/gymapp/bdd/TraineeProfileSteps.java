package com.dre.gymapp.bdd;

import com.dre.gymapp.dto.trainee.TraineeProfileResponse;
import com.dre.gymapp.dto.trainee.TraineeProfileUpdateRequest;
import com.dre.gymapp.dto.user.ActivationRequest;
import com.dre.gymapp.exception.NotFoundException;
import com.dre.gymapp.service.TraineeService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TraineeProfileSteps {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TraineeService traineeService;

    private ResultActions resultActions;

    @Given("a trainee with username {string} exists")
    public void trainee_with_username_exists(String username) {
        TraineeProfileResponse traineeProfileResponse = new TraineeProfileResponse();
        traineeProfileResponse.setFirstName("John");
        traineeProfileResponse.setLastName("Doe");
        when(traineeService.getTraineeProfileByUsername(username))
                .thenReturn(traineeProfileResponse);
    }

    @Given("a trainee with username {string} does not exist")
    public void trainee_with_username_does_not_exist(String username) {
        when(traineeService.getTraineeProfileByUsername(username))
                .thenThrow(NotFoundException.class);
    }

    @When("a user sends a GET request to {string}")
    public void the_user_sends_a_get_request_to(String url) throws Exception {
        this.resultActions = this.mockMvc.perform(get(url).with(user("john.doe")));
    }

    @Then("the trainee profile response status should be {int}")
    public void the_trainee_profile_response_status_should_be(int statusCode) throws Exception {
        this.resultActions.andExpect(status().is(statusCode));
    }


    @Given("the trainee service will successfully update the profile for {string}")
    public void the_trainee_service_will_successfully_update_the_profile_for(String username) {
        TraineeProfileResponse traineeProfileResponse = new TraineeProfileResponse();
        traineeProfileResponse.setFirstName("Updated");
        traineeProfileResponse.setLastName("Name");
        when(traineeService.updateTrainee(eq(username), any(TraineeProfileUpdateRequest.class)))
                .thenReturn(traineeProfileResponse);
    }

    @When("a user sends a PUT request to {string} with:")
    public void a_user_sends_a_put_request_to_with(String url, Map<String, String> data) throws Exception {
        String jsonBody = objectMapper.writeValueAsString(data);
        this.resultActions = this.mockMvc.perform(put(url)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("john.doe")));
    }

    @And("the response body should contain the trainee's first name {string}")
    public void the_response_body_should_contain_the_trainees_first_name(String firstName) throws Exception {
        resultActions.andExpect(jsonPath("$.firstName").value(firstName));
    }


    @Given("the trainee service is ready to delete {string}")
    public void the_trainee_service_is_ready_to_delete(String username) {
        doNothing().when(traineeService).deleteTraineeByUsername(username);
    }

    @When("a user sends a DELETE request to {string}")
    public void a_user_sends_a_delete_request_to(String url) throws Exception {
        this.resultActions = this.mockMvc.perform(delete(url)
                .with(user("john.doe")));
    }

    @Given("the trainee service is ready to update activation status for {string}")
    public void the_trainee_service_is_ready_to_update_activation_status_for(String username) {
        doNothing().when(traineeService).setTraineeActiveStatus(username, true);
    }


    @When("a user sends a PATCH request to {string} with status {string}")
    public void a_user_sends_a_patch_request_to_with_status(String url, String status) throws Exception {
        ActivationRequest request = new ActivationRequest(Boolean.parseBoolean(status));
        String jsonBody = objectMapper.writeValueAsString(request);
        this.resultActions = this.mockMvc.perform(patch(url)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("john.doe")));
    }


    @And("the trainee service activation method should be called with {string}")
    public void the_trainee_service_activation_method_should_be_called_with(String status) {
        verify(traineeService, times(1)).setTraineeActiveStatus(anyString(), eq(Boolean.parseBoolean(status)));
    }

    @Given("the trainee service will not find {string}")
    public void the_trainee_service_will_not_find(String username) {
        doThrow(new NotFoundException("Trainee not found"))
                .when(traineeService).setTraineeActiveStatus(eq(username), any(Boolean.class));
    }
}