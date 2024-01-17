package com.aerotrack.console.resultconsole;

import com.aerotrack.console.welcomeconsole.AerotrackApp;
import com.aerotrack.model.entities.Trip;
import lombok.Setter;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;
import java.util.Map;

public class DestinationsButtonsPanel extends JPanel{

    private final AerotrackApp parent;
    @Setter
    private Map<String, List<Trip>> destinationResults;

    public DestinationsButtonsPanel(AerotrackApp parent, Map<String, List<Trip>> destinationResults) {
        this.parent = parent;
        this.destinationResults = destinationResults;
    }

    public void initComponents() {
        removeAll();
        setLayout(new FlowLayout(FlowLayout.CENTER,10,25));

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(0, 3));

        int marginSize = 10;
        parent.getRootPane().setBorder(new EmptyBorder(marginSize, marginSize, marginSize, marginSize));
        buttonsPanel.setBorder(new EmptyBorder(marginSize, marginSize, marginSize, marginSize));

        for (String destination : destinationResults.keySet()) {
            JButton destinationButton = new JButton("<html>Flight to " + destination + "<br>Starting from " + destinationResults.get(destination).get(0).getTotalPrice() + "€</html>");

            destinationButton.setPreferredSize(new Dimension(200, 70));
            destinationButton.setMargin(new Insets(10, 10, 10, 10));
            destinationButton.addActionListener(e -> showDestinationResults(destination));
            buttonsPanel.add(destinationButton);
        }

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(new EmptyBorder(marginSize, marginSize, marginSize, marginSize));
        JButton closeButton = new JButton("New Research");
        closeButton.setBackground(Color.CYAN);
        closeButton.setForeground(Color.BLACK);
        closeButton.addActionListener(e -> {
            parent.showMainPanel();
        });
        bottomPanel.add(closeButton);
        add(buttonsPanel);
        add(bottomPanel);
    }

    private void showDestinationResults(String destination) {
        DestinationResultFrame destinationResultFrame = new DestinationResultFrame(destinationResults.get(destination));
        destinationResultFrame.setVisible(true);
    }
}