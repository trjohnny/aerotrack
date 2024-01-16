package com.aerotrack.console.welcomeconsole.components;

import com.aerotrack.console.welcomeconsole.ScanInputView;
import lombok.Getter;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Objects;


public class ButtonManager {
    @Getter
    private final JPanel panel;
    private final ActionHandler actionHandler;


    public ButtonManager(ScanInputView parent, InputPanel inputPanel) {
        actionHandler = new ActionHandler(inputPanel, parent);
        JLabel loadingLabel = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("/loading-buffering.gif"))));

        panel = new JPanel(new BorderLayout());

        JPanel departurePanel = new JPanel(new GridLayout(0, 1));
        JPanel destinationPanel = new JPanel(new GridLayout(0, 1));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton removeDepartureAirportButton = new JButton("Remove Departure Airport");
        JButton addDepartureAirportButton = new JButton("Add Departure Airport");
        JButton removeDestinationAirportButton = new JButton("Remove Destinations");
        JButton addDestinationAirportButton = new JButton("Add Destinations");

        setupAirportControls(parent, inputPanel, departurePanel, addDepartureAirportButton, removeDepartureAirportButton, true);
        setupAirportControls(parent, inputPanel, destinationPanel, addDestinationAirportButton, removeDestinationAirportButton, false);

        JButton submitButton = new JButton("Submit");
        loadingLabel.setVisible(false);

        submitButton.addActionListener(e -> actionHandler.submitFlightInfo(parent, buttonPanel));

        buttonPanel.add(submitButton);
        buttonPanel.add(loadingLabel);

        panel.add(departurePanel, BorderLayout.WEST);
        panel.add(destinationPanel, BorderLayout.EAST);
        panel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupAirportControls(ScanInputView parent, InputPanel inputPanel, JPanel airportControlsPanel, JButton addButton, JButton removeButton, boolean isDeparture) {
        removeButton.setEnabled(false);
        removeButton.addActionListener(e -> {
            if (isDeparture) {
                inputPanel.removeDepartureBox(inputPanel.getInnerDeparturePanel());
                addButton.setEnabled(true);
            } else {
                inputPanel.removeDestinationBox(inputPanel.getInnerDestinationPanel());
                addButton.setEnabled(true);
            }
            int departureCount = inputPanel.getDepartureAirportsComboBoxes().size();
            int destinationCount = inputPanel.getDestinationAirportsComboBoxes().size();
            removeButton.setEnabled(isDeparture ? departureCount > 0 : destinationCount > 0);

            parent.validate();
            parent.repaint();
        });

        addButton.addActionListener(e -> {
            if (isDeparture) {
                int departureCount = inputPanel.getDepartureAirportsComboBoxes().size();
                if (departureCount < 5) {
                    inputPanel.addDepartureBox(inputPanel.getInnerDeparturePanel());
                    departureCount++;
                }
                removeButton.setEnabled(true);
                addButton.setEnabled(departureCount < 5);
            } else {
                inputPanel.addDestinationBox(inputPanel.getInnerDestinationPanel());
                int destinationCount = inputPanel.getDestinationAirportsComboBoxes().size();
                removeButton.setEnabled(destinationCount > 0);
                addButton.setEnabled(destinationCount < Integer.MAX_VALUE);
            }
            parent.validate();
            parent.repaint();
        });

        airportControlsPanel.add(addButton);
        airportControlsPanel.add(removeButton);
    }

    public JPanel getPanel() {
        return panel;
    }
}

