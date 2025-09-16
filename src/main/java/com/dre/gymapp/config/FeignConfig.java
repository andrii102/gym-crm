package com.dre.gymapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Contract;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@Configuration
public class FeignConfig {

    @Bean
    public Contract feignContract() {
        return new SpringMvcContract();
    }

    @Bean
    public Encoder feignEncoder(ObjectMapper objectMapper) {
        return new SpringEncoder(() -> new HttpMessageConverters(
                new MappingJackson2HttpMessageConverter(objectMapper)
        ));
    }

    @Bean
    public Decoder feignDecoder(ObjectMapper objectMapper) {
        return new SpringDecoder(() -> new HttpMessageConverters(
                new MappingJackson2HttpMessageConverter(objectMapper)
        ));
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getCredentials() instanceof String jwt) {
                requestTemplate.header("Authorization", "Bearer " + jwt);
            }

            String txId = MDC.get("transactionId");
            if (txId != null) {
                requestTemplate.header("X-Transaction-Id", txId);
            }
        };
    }
}
