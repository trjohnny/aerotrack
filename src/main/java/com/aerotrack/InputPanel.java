// InputPanel contiene il codice per la costruzione del riquadro degli input
package com.aerotrack;

import javax.swing.*;
import java.awt.*;

public class InputPanel {
    private final JPanel panel;
    private final AerotrackApp parent;
    private final FlightInfoFields flightInfoFields;

    public InputPanel(AerotrackApp parent, FlightInfoFields flightInfoFields) {
        this.parent = parent;
        this.flightInfoFields = flightInfoFields;

        panel = new JPanel(new GridLayout(0, 2, 5, 5));

        // Initialize the list of departure fields
        addDepartureField();

        JLabel startDateLabel = new JLabel("Start Date (yyyy-MM-dd):");
        JTextField startDateField = flightInfoFields.getStartDateField();
        panel.add(startDateLabel);
        panel.add(startDateField);

        JLabel endDateLabel = new JLabel("End Date (yyyy-MM-dd):");
        JTextField endDateField = flightInfoFields.getEndDateField();
        panel.add(endDateLabel);
        panel.add(endDateField);

        JLabel durationLabel = new JLabel("Duration (days):");
        JTextField durationField = flightInfoFields.getDurationField();
        panel.add(durationLabel);
        panel.add(durationField);
    }

    public JPanel getPanel() {
        return panel;
    }

    public void addDepartureField() {
        JLabel departureLabel = new JLabel("Departure Airport:");
        JTextField departureField = new JTextField(10);
        flightInfoFields.getDepartureFields().add(departureField);

        int startDateIndex = -1;
        Component[] components = panel.getComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof JLabel label) {
                if ("Start Date (yyyy-MM-dd):".equals(label.getText())) {
                    startDateIndex = i;
                    break;
                }
            }
        }

        if (startDateIndex != -1) {
            panel.add(departureLabel, startDateIndex);
            panel.add(departureField, startDateIndex + 1);
        } else {
            panel.add(departureLabel);
            panel.add(departureField);
        }

        parent.validate();
        parent.repaint();
    }

    public void removeDepartureField() {
        int lastIndex = flightInfoFields.getDepartureFields().size() - 1;
        if (lastIndex >= 0) {
            panel.remove(lastIndex * 2); // Remove label
            panel.remove(lastIndex * 2); // Remove text field

            flightInfoFields.getDepartureFields().remove(lastIndex);

            panel.revalidate();
            panel.repaint();
        }
    }
}
