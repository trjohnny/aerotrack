// FlightInfoFields tiene traccia di tutti field in cui vengono inseriti i dati in input
package com.aerotrack;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class FlightInfoFields {
    private final List<JTextField> departureFields;
    private final JTextField startDateField;
    private final JTextField endDateField;
    private final JTextField durationField;


    public FlightInfoFields() {
        departureFields = new ArrayList<>();
        startDateField = new JTextField(10);
        endDateField = new JTextField(10);
        durationField = new JTextField(10);
    }

    public JTextField getStartDateField() {
        return startDateField;
    }
    public JTextField getEndDateField() {
        return endDateField;
    }
    public JTextField getDurationField() {
        return durationField;
    }
    public List<JTextField> getDepartureFields() {
        return departureFields;
    }
    public String getDepartureAirports() {
        StringBuilder airports = new StringBuilder();
        for (JTextField departureField : departureFields) {
            String departureAirport = departureField.getText().trim();
            if (!departureAirport.isEmpty()) {
                airports.append(departureAirport).append(", ");
            }
        }
        if (airports.length() > 2) {
            airports.setLength(airports.length() - 2); // Remove the trailing comma and space
        }
        return airports.toString();
    }
}
