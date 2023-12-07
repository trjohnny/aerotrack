package com.aerotrack.client;

import com.aerotrack.model.ScanQueryRequest;
import com.aerotrack.model.ScanQueryResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiGatewayClient {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String endpointUrl; // API Gateway Endpoint URL
    private final String apiKey; // API Key for the API Gateway

    public ApiGatewayClient(String endpointUrl, String apiKey) {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.endpointUrl = endpointUrl;
        this.apiKey = apiKey;
    }

    public ScanQueryResponse sendScanQueryRequest(ScanQueryRequest scanQueryRequest) {
        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(scanQueryRequest);
        } catch (Exception e) {
            log.error("Error serializing ScanQueryRequest: " + e.getMessage());
            return null; // Handle serialization error
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpointUrl))
                .header("x-api-key", apiKey) // Set API Key Header if required
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        log.info(requestBody);

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), ScanQueryResponse.class);
            } else {
                log.error("Failed to get response: Status Code = " + response.statusCode());
                return null; // Handle non-200 responses as needed
            }
        } catch (Exception e) {
            log.error("Error in POST request: " + e.getMessage());
            return null; // Handle exception as needed
        }
    }
}
