package com.dre.gymapp.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@Import({SpringDocConfig.class , ManualSwaggerConfig.class})
@ComponentScan(basePackages = "com.dre.gymapp.controller")
public class WebConfig implements WebMvcConfigurer {
//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule());
//        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        converters.add(new MappingJackson2HttpMessageConverter());
//    }

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
