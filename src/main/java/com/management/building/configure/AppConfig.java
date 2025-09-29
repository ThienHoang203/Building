package com.management.building.configure;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class AppConfig {
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // Thêm error handler
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(@SuppressWarnings("null") ClientHttpResponse response) throws IOException {
                return response.getStatusCode().is4xxClientError() ||
                        response.getStatusCode().is5xxServerError();
            }

            @Override
            public void handleError(@SuppressWarnings("null") ClientHttpResponse response) throws IOException {
                String responseBody = StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8);
                log.error("API Error - Status: {}, Body: {}", response.getStatusCode(), responseBody);
                throw new HttpClientErrorException(response.getStatusCode(), responseBody);
            }
        });

        return restTemplate;
    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
