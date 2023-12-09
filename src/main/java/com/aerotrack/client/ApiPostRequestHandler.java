package com.aerotrack.client;

import com.aerotrack.Exceptions.ApiRequestException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiPostRequestHandler {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ApiPostRequestHandler() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public <T> T sendPostRequest(String endpointUrl, String apiKey, Object requestObject, Class<T> responseType) {
        try {
            String requestBody = objectMapper.writeValueAsString(requestObject);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpointUrl))
                    .header("x-api-key", apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            log.info(requestBody);

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), responseType);
            } else {
                log.error("Failed to get response: Status Code = " + response.statusCode());
                throw new ApiRequestException("Response status code: " + response.statusCode());
            }
        } catch (JsonProcessingException e) {
            log.error("Error serializing/deserializing JSON: " + e.getMessage());
            throw new ApiRequestException("JSON processing error", e);
        } catch (IOException | InterruptedException e) {
            log.error("Error in API request: " + e.getMessage());
            throw new ApiRequestException("API request error", e);
        }
    }

}
