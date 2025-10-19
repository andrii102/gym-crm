package com.dre.gymapp.bdd;

import com.dre.gymapp.dto.trainings.NewTrainingRequest;
import com.dre.gymapp.dto.trainings.TrainingTypeResponse;
import com.dre.gymapp.model.Training;
import com.dre.gymapp.service.TrainingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TrainingSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TrainingService trainingService;

    @Autowired
    private ObjectMapper objectMapper;

    private ResultActions resultActions;

    @Given("the training service has a list of training types")
    public void the_training_service_has_a_list_of_training_types() {
        List<TrainingTypeResponse> trainingTypes = List.of(
                new TrainingTypeResponse(1L, "Cardio"),
                new TrainingTypeResponse(2L, "Yoga")
        );
        when(trainingService.getAllTrainingTypes()).thenReturn(trainingTypes);
    }

    @When("a user sends a GET request to {string} for trainings")
    public void user_sends_get_request_to(String url) throws Exception {
        this.resultActions = this.mockMvc.perform(get(url).with(user("john.doe")));
    }


    @Then("the training response status should be {int}")
    public void training_response_status_should_be(int status) throws Exception {
        this.resultActions.andExpect(status().is(status));
        System.out.println(resultActions.andReturn().getResponse().getContentAsString());
    }

    @And("the response body should contain a list of training types")
    public void response_body_should_contain_list_of_training_types() throws Exception {
        resultActions.andExpect(jsonPath("$[0].trainingType").value("Cardio"))
                .andExpect(jsonPath("$[1].trainingType").value("Yoga"));
    }

    @Given("the training service is ready to create a new training")
    public void training_service_is_ready_to_create_new_training() {
        doReturn(new Training()).when(trainingService).createTraining(any(NewTrainingRequest.class));
    }

    @When("a user sends a POST request to {string} with training data:")
    public void user_sends_post_request_to_with_training_data(String url, Map<String, String> data) throws Exception {
        String jsonBody = this.objectMapper.writeValueAsString(data);

        this.resultActions = this.mockMvc.perform(post(url)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("john.doe")));
    }

    @And("the training service create method should be called")
    public void training_service_create_method_should_be_called() {
        verify(trainingService, times(1)).createTraining(any(NewTrainingRequest.class));
    }
}