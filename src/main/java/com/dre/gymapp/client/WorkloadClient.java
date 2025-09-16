package com.dre.gymapp.client;

import com.dre.gymapp.config.FeignConfig;
import com.dre.gymapp.dto.trainings.TrainingEventRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "trainer-workload-service", configuration = FeignConfig.class)
public interface WorkloadClient {
    @PostMapping(value = "/api/v1/workloads/training", consumes = "application/json")
    String manageTraining(@RequestBody TrainingEventRequest request);
}
