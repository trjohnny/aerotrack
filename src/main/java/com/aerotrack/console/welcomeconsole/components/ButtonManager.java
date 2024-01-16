// ButtonManager si occupa di creazione+logica dei bottoni
package com.aerotrack.console.welcomeconsole.components;

import com.aerotrack.console.welcomeconsole.ScanInputView;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;


public class ButtonManager {
    private final JPanel panel;
    private final ActionHandler actionHandler;


    public ButtonManager(ScanInputView parent, InputPanel inputPanel, JTextPane textPane) {
        actionHandler = new ActionHandler(inputPanel);
        panel = new JPanel(new BorderLayout());

        JPanel departurePanel = new JPanel(new GridLayout(0, 1));
        JPanel destinationPanel = new JPanel(new GridLayout(0, 1));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton removeDepartureAirportButton = new JButton("Remove Departure Airport");
        JButton addDepartureAirportButton = new JButton("Add Departure Airport");
        JButton removeDestinationAirportButton = new JButton("Remove Destinations");
        JButton addDestinationAirportButton = new JButton("Add Destinations");

        // Initialize departure airport controls
        setupAirportControls(parent, inputPanel, departurePanel, addDepartureAirportButton, removeDepartureAirportButton, true);

        // Initialize destination airport controls
        setupAirportControls(parent, inputPanel, destinationPanel, addDestinationAirportButton, removeDestinationAirportButton, false);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> actionHandler.submitFlightInfo(parent, textPane));
        buttonPanel.add(submitButton);

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

            // Abilita/disabilita il pulsante di rimozione in base al conteggio attuale
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

                // Abilita/disabilita il pulsante di rimozione in base al conteggio attuale
                removeButton.setEnabled(true);

                // Abilita/disabilita il pulsante di aggiunta in base al conteggio attuale
                addButton.setEnabled(departureCount < 5);
            } else {
                inputPanel.addDestinationBox(inputPanel.getInnerDestinationPanel());

                int destinationCount = inputPanel.getDestinationAirportsComboBoxes().size();

                // Abilita/disabilita il pulsante di rimozione in base al conteggio attuale
                removeButton.setEnabled(destinationCount > 0);

                // Abilita/disabilita il pulsante di aggiunta in base al conteggio attuale
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

