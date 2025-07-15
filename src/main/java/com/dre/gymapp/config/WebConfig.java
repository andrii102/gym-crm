package com.dre.gymapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@Import({SpringDocConfig.class , ManualSwaggerConfig.class})
@ComponentScan(basePackages = {"com.dre.gymapp.controller", "com.dre.gymapp.aspect"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // Map swagger-ui.html to the actual index.html in the WebJar
        registry.addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/")
                .setCachePeriod(3600);

        // All webjars resources
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
                .setCachePeriod(3600);

    }
}
