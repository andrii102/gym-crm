package com.dre.gymapp.config;

import org.springdoc.core.customizers.SpringDocCustomizers;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.SpringDocProviders;
import org.springdoc.core.service.AbstractRequestService;
import org.springdoc.core.service.GenericResponseService;
import org.springdoc.core.service.OpenAPIService;
import org.springdoc.core.service.OperationService;
import org.springdoc.webmvc.api.OpenApiWebMvcResource;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ManualSwaggerConfig implements WebMvcConfigurer {

    @Bean
    public OpenApiWebMvcResource openApiWebMvcResource(
            ObjectFactory<OpenAPIService> openAPIServiceFactory,
            AbstractRequestService requestService,
            GenericResponseService responseService,
            OperationService operationService,
            SpringDocConfigProperties springDocConfigProperties,
            SpringDocProviders springDocProviders,
            SpringDocCustomizers springDocCustomizers
    ) {
        return new OpenApiWebMvcResource(
                openAPIServiceFactory,
                requestService,
                responseService,
                operationService,
                springDocConfigProperties,
                springDocProviders,
                springDocCustomizers
        );
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/api/**")
                .build();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Redirect root to swagger-ui
        registry.addRedirectViewController("/", "/swagger-ui.html");
        registry.addRedirectViewController("/swagger-ui", "/swagger-ui.html");
    }

}
