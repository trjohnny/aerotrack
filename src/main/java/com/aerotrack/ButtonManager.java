// ButtonManager si occupa di creazione+logica dei bottoni
package com.aerotrack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonManager {
    private final JPanel panel;
    private final FlightInfoFields flightInfoFields;

    public ButtonManager(AerotrackApp parent, InputPanel airportPanel, FlightInfoFields flightInfoFields) {
        this.flightInfoFields = flightInfoFields;

        panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton removeAirportButton = new JButton("Remove Airport");
        removeAirportButton.setEnabled(false);
        removeAirportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                airportPanel.removeDepartureField();
                if (ButtonManager.this.flightInfoFields.getDepartureFields().size() == 1) {
                    removeAirportButton.setEnabled(false);
                }
                parent.resizeWindow();
            }
        });

        JButton addAirportButton = new JButton("Add Airport");
        addAirportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (flightInfoFields.getDepartureFields().size() < 5){
                airportPanel.addDepartureField();
                removeAirportButton.setEnabled(true);
                parent.validate();
                parent.repaint();
                parent.resizeWindow();
                } else {
                    JOptionPane.showMessageDialog(parent,
                            "You can add up to 5 departure airports.",
                            "Limit Reached",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.submitFlightInfo(flightInfoFields);
            }
        });

        panel.add(removeAirportButton);
        panel.add(addAirportButton);
        panel.add(submitButton);
    }

    public JPanel getPanel() {
        return panel;
    }
}
