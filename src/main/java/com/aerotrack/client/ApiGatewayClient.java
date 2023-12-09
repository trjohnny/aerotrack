package com.aerotrack.client;

import com.aerotrack.Exceptions.AerotrackClientException;
import com.aerotrack.Exceptions.ApiRequestException;
import com.aerotrack.model.FlightPair;
import com.aerotrack.model.ScanQueryRequest;
import com.aerotrack.model.ScanQueryResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ApiGatewayClient {

    private final ApiPostRequestHandler apiPostRequestHandler;

    public ApiGatewayClient() {
        this.apiPostRequestHandler = new ApiPostRequestHandler();
    }

    public List<FlightPair> getBestFlight(String startDateString,String endDateString,String minDurationString,String maxDurationString,List<String> departureAirports,Boolean returnToSameAirport, List<String> destinationAirports){
        ScanQueryRequest scanQueryRequest = buildScanQueryRequest(startDateString,endDateString,minDurationString,maxDurationString,departureAirports,returnToSameAirport,destinationAirports);
        return sendScanQueryRequest(scanQueryRequest).getFlightPairs();
    }

    public ScanQueryRequest buildScanQueryRequest(String startDateString,String endDateString,String minDurationString,String maxDurationString,List<String> departureAirports,Boolean returnToSameAirport, List<String> destinationAirports) {
        ScanQueryRequest.ScanQueryRequestBuilder builder = ScanQueryRequest.builder();

        // Imposta i valori dal flightInfoFields
        builder.minDays(Integer.valueOf(minDurationString));
        builder.maxDays(Integer.valueOf(maxDurationString));
        builder.availabilityStart(startDateString);
        builder.availabilityEnd(endDateString);
        builder.departureAirports(departureAirports);
        builder.returnToSameAirport(returnToSameAirport);
        builder.destinationAirports(destinationAirports);
        return builder.build();
    }
    public ScanQueryResponse sendScanQueryRequest(ScanQueryRequest scanQueryRequest) {
        try {
            // API Gateway Endpoint URL
            String endpointUrl = "https://f1muce19kh.execute-api.eu-west-1.amazonaws.com/prod/scan";
            // API Key for the API Gateway
            String apiKey = "z9inDLaWtOamHqvOCl25w33KtSbqVpOf61oPHGhK";
            return apiPostRequestHandler.sendPostRequest(endpointUrl, apiKey, scanQueryRequest, ScanQueryResponse.class);
        } catch (ApiRequestException e) {
            log.error("Error in API request: " + e.getMessage());
            throw new AerotrackClientException(e); // Rilancia l'eccezione per consentire la gestione a livelli superiori, se necessario
        }
    }
}



