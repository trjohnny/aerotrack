package com.aerotrack.client;

import com.aerotrack.model.ScanQueryRequest;
import com.aerotrack.model.ScanQueryResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiGatewayClient {

    private final ApiRequestHandler apiRequestHandler;
    private final String endpointUrl = "https://f1muce19kh.execute-api.eu-west-1.amazonaws.com/prod/scan"; // API Gateway Endpoint URL
    private final String apiKey = "z9inDLaWtOamHqvOCl25w33KtSbqVpOf61oPHGhK"; // API Key for the API Gateway

    public ApiGatewayClient() {
        this.apiRequestHandler = new ApiRequestHandler();
    }

    public ScanQueryResponse sendScanQueryRequest(ScanQueryRequest scanQueryRequest) {
        try {
            return apiRequestHandler.sendRequest(endpointUrl, apiKey, scanQueryRequest, ScanQueryResponse.class);
        } catch (ApiRequestException e) {
            log.error("Error in API request: " + e.getMessage());
            throw e; // Rilancia l'eccezione per consentire la gestione a livelli superiori, se necessario
        }
    }
}



