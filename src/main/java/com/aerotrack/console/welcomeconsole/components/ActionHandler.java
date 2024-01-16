package com.aerotrack.console.welcomeconsole.components;

import com.aerotrack.console.resultconsole.DestinationsButtonsView;
import com.aerotrack.model.entities.Airport;
import com.aerotrack.model.protocol.ScanQueryRequest;
import com.aerotrack.console.welcomeconsole.AerotrackApp;
import com.aerotrack.model.entities.Trip;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.jdesktop.swingx.JXComboBox;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import static com.aerotrack.utils.Utils.countryNameToCodeMap;
import static com.aerotrack.utils.Utils.convertDate;


@Slf4j
public class ActionHandler {
    private final AerotrackApp parent;
    private final InputPanel inputPanel;

    private final Map<String, List<Trip>> destinationResults = new LinkedHashMap<>();


    public ActionHandler(InputPanel inputPanel, AerotrackApp parent){
        this.inputPanel = inputPanel;
        this.parent = parent;
    }


    public void submitFlightInfo(AerotrackApp parent, JPanel buttonPanel) {
        String startDateString;
        String endDateString;
        try {
            startDateString = convertDate(inputPanel.getStartDatePicker().getText());
            endDateString = convertDate(inputPanel.getEndDatePicker().getText());
        } catch (ParseException e){
            JOptionPane.showMessageDialog(parent,"Insert the dates in then correct way: dd MMMM yyyy", "Format Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String minDurationString = inputPanel.getMinDaysField().getText();
        String maxDurationString = inputPanel.getMaxDaysField().getText();

        List<String> departureAirports = new ArrayList<>();
        for (JXComboBox departureField : inputPanel.getDepartureAirportsComboBoxes()) {
            String departureAirport = (String) departureField.getSelectedItem();
            if (departureAirport != null && !departureAirport.trim().isEmpty()) {
                if (departureAirport.contains("[") && departureAirport.contains("]")) {
                    int startIdx = departureAirport.indexOf("[") + 1;
                    int endIdx = departureAirport.indexOf("]");
                    String airportCode = departureAirport.substring(startIdx, endIdx);
                    departureAirports.add(airportCode);
                }
            }
        }

        Set<String> destinations = new HashSet<>();
        Set<String> destinationAirportsCodes = new HashSet<>();
        for (JXComboBox destinationField : inputPanel.getDestinationAirportsComboBoxes()) {
            String destinationAirport = (String) destinationField.getSelectedItem();
            if (destinationAirport != null && !destinationAirport.trim().isEmpty()) {
                if(countryNameToCodeMap.containsKey(destinationAirport)){
                    destinations.add(countryNameToCodeMap.get(destinationAirport));
                }
                if (destinationAirport.contains("[") && destinationAirport.contains("]")) {
                    int startIdx = destinationAirport.indexOf("[") + 1;
                    int endIdx = destinationAirport.indexOf("]");
                    String airportCode = destinationAirport.substring(startIdx, endIdx);
                    destinationAirportsCodes.add(airportCode);
                }
            }
        }
        for (String countryCode : destinations){
            for (Airport airport : inputPanel.airportsJsonFile.getAirports()){
                if(countryCode.equals(airport.getCountryCode())){
                    destinationAirportsCodes.add(airport.getAirportCode());
                }
            }
        }
        List<String> destinationAirports = new ArrayList<>(destinationAirportsCodes);

        if (startDateString.isEmpty() || endDateString.isEmpty() || Objects.isNull(minDurationString) ||
                minDurationString.isEmpty() || Objects.isNull(maxDurationString) || maxDurationString.isEmpty() ||
                departureAirports.isEmpty() || destinationAirports.isEmpty()) {
            JOptionPane.showMessageDialog(parent,"Please fill in all fields.", "Fields Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Set<String> duplicateDepartures = findDuplicates(departureAirports);
        Set<String> duplicateDestinations = findDuplicates(destinationAirports);

        if (!duplicateDepartures.isEmpty() || !duplicateDestinations.isEmpty()) {
            StringBuilder errorMessageBuilder = new StringBuilder("Error: Duplicate airports detected.\n");

            if (!duplicateDepartures.isEmpty()) {
                String duplicateDeparturesString = String.join(", ", duplicateDepartures);
                errorMessageBuilder.append("Duplicate departure airports: ").append(duplicateDeparturesString).append("\n");
            }

            if (!duplicateDestinations.isEmpty()) {
                String duplicateDestinationsString = String.join(", ", duplicateDestinations);
                errorMessageBuilder.append("Duplicate destination airports: ").append(duplicateDestinationsString);
            }

            JOptionPane.showMessageDialog(parent, errorMessageBuilder.toString(), "Duplicate Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        ScanQueryRequest scanQueryRequest = buildScanQueryRequest(minDurationString, maxDurationString, startDateString, endDateString, departureAirports, inputPanel.getReturnToSameAirportCheckBox().isSelected(), destinationAirports);
        if (scanQueryRequest == null){
            return;
        }

        SwingWorker<List<Trip>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Trip> doInBackground() {
                return parent.getAerotrackApiClient().getBestFlight(scanQueryRequest);
            }

            @Override
            protected void done() {
                try {
                    List<Trip> tripList = get();
                    if (tripList != null) {
                        organizeResultsByDestination(tripList);
                        DestinationsButtonsView buttonsView = new DestinationsButtonsView(parent, destinationResults);
                        parent.setVisible(false);
                        buttonsView.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(parent,"Error: Failed to get valid response from API.", "Response Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(parent,"Error: An exception occurred during API call.", "Response Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    buttonPanel.getComponent(0).setEnabled(true);
                    buttonPanel.getComponent(1).setVisible(false);
                }
            }
        };
        buttonPanel.getComponent(0).setEnabled(false);
        buttonPanel.getComponent(1).setVisible(true);
        worker.execute();
    }

    public ScanQueryRequest buildScanQueryRequest(String minDurationString, String maxDurationString,String startDateString, String endDateString, List<String> departureAirports, Boolean returnToSameAirport, List<String> destinationAirports) {
        try {
            ScanQueryRequest.ScanQueryRequestBuilder builder = ScanQueryRequest.builder();
            builder.minDays(Integer.valueOf(minDurationString));
            builder.maxDays(Integer.valueOf(maxDurationString));
            builder.availabilityStart(startDateString);
            builder.availabilityEnd(endDateString);
            builder.departureAirports(departureAirports);
            builder.destinationAirports(destinationAirports);
            builder.returnToSameAirport(returnToSameAirport);
            return builder.build();
        } catch (IllegalArgumentException e){
            JOptionPane.showMessageDialog(parent, e.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public static Set<String> findDuplicates(Collection<String> airports) {
        Set<String> uniqueAirports = new HashSet<>();
        return airports.stream()
                .filter(airport -> !uniqueAirports.add(airport))
                .collect(Collectors.toSet());
    }

    private void organizeResultsByDestination(List<Trip> tripList) {
        destinationResults.clear();
        for (Trip trip : tripList) {
            String direction = trip.getOutboundFlights().get(0).getDirection();
            destinationResults.computeIfAbsent(direction.split("-")[1], k -> new ArrayList<>());
            destinationResults.get(direction.split("-")[1]).add(trip);
        }
    }

}
