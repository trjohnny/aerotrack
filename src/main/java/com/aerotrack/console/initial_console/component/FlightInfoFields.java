// FlightInfoFields tiene traccia di tutti field in cui vengono inseriti i dati in input
package com.aerotrack.console.initial_console.component;

import javax.swing.JTextField;
import javax.swing.JCheckBox;
import java.util.ArrayList;
import java.util.List;

public class FlightInfoFields {
    private final List<JTextField> departureFields;
    private final JTextField startDateField;
    private final JTextField endDateField;
    private final JTextField minDaysField;
    private final JTextField maxDaysField;
    private final JCheckBox returnToSameAirportCheckBox;


    public FlightInfoFields() {
        departureFields = new ArrayList<>();
        startDateField = new JTextField(10);
        startDateField.setText("2024-05-01");
        endDateField = new JTextField(10);
        endDateField.setText("2024-07-15");
        minDaysField = new JTextField(10);
        minDaysField.setText("5");
        maxDaysField = new JTextField(10);
        maxDaysField.setText("15");
        returnToSameAirportCheckBox = new JCheckBox("Return to the same airport", false);

    }

    public JTextField getStartDateField() {
        return startDateField;
    }
    public JTextField getEndDateField() {
        return endDateField;
    }
    public JTextField getMinDaysField() {
        return minDaysField;
    }
    public JTextField getMaxDaysField() {
        return maxDaysField;
    }
    public JCheckBox getReturnToSameAirportCheckBox() {
        return returnToSameAirportCheckBox;
    }


    public Boolean getReturnToSameAirportCheckBoxValue() {
        return returnToSameAirportCheckBox.isSelected();
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
