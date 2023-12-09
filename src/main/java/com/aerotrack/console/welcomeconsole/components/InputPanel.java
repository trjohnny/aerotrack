// InputPanel contiene il codice per la costruzione del riquadro degli input
package com.aerotrack.console.welcomeconsole.components;

import com.aerotrack.console.welcomeconsole.ScanInputView;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import java.awt.GridLayout;
import java.awt.Component;

public class InputPanel {
    private final JPanel panel;
    private final ScanInputView parent;
    private final FlightInfoFields flightInfoFields;

    public InputPanel(ScanInputView parent, FlightInfoFields flightInfoFields) {
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

        JLabel minDaysDurationLabel = new JLabel("Min duration of the trip (days):");
        JTextField minDaysDurationField = flightInfoFields.getMinDaysField();
        panel.add(minDaysDurationLabel);
        panel.add(minDaysDurationField);

        JLabel maxDaysDurationLabel = new JLabel("Max duration of the trip  (days):");
        JTextField maxDaysDurationField = flightInfoFields.getMaxDaysField();
        panel.add(maxDaysDurationLabel);
        panel.add(maxDaysDurationField);

        JCheckBox returnToSameAirportCheckBox = flightInfoFields.getReturnToSameAirportCheckBox();
        panel.add(returnToSameAirportCheckBox);
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
