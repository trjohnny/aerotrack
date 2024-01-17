package com.aerotrack.console.resultconsole;

import com.aerotrack.console.welcomeconsole.AerotrackApp;
import com.aerotrack.model.entities.Airport;
import com.aerotrack.model.entities.AirportsJsonFile;
import com.aerotrack.model.entities.Trip;
import lombok.Setter;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DestinationsButtonsPanel extends JPanel{

    private final AerotrackApp parent;
    public final AirportsJsonFile airportsJsonFile;
    @Setter
    private Map<String, List<Trip>> destinationResults;


    public DestinationsButtonsPanel(AerotrackApp parent, Map<String, List<Trip>> destinationResults, AirportsJsonFile airportsJsonFile) {
        this.parent = parent;
        this.airportsJsonFile = airportsJsonFile;
        this.destinationResults = destinationResults;
    }

    public void initComponents() {
        removeAll();
        setLayout(new BorderLayout());

        parent.getRootPane().setBorder(new EmptyBorder(10, 15, 10, 15));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel buttonsPanel = new JPanel(new GridLayout(0, 4, 10, 10));
        buttonsPanel.setAlignmentY(Component.TOP_ALIGNMENT);

        Map<String, Airport> mapAirportCodeToName = airportsJsonFile.getAirports().stream()
                .collect(Collectors.toMap(Airport::getAirportCode, Function.identity()));

        for (String airportCode : destinationResults.keySet()) {
            JButton destinationButton = new JButton("<html>Flights to " + mapAirportCodeToName.get(airportCode).getName() + "<br>Starting from " + destinationResults.get(airportCode).get(0).getTotalPrice() + "€</html>");

            destinationButton.setPreferredSize(new Dimension(195, 70));
            destinationButton.setMargin(new Insets(10, 10, 10, 10));
            destinationButton.addActionListener(e -> showDestinationResults(airportCode));

            buttonsPanel.add(destinationButton);
        }

        JPanel buttonsContainerPanel = new JPanel();
        buttonsContainerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonsContainerPanel.add(buttonsPanel);

        JScrollPane scrollPane = new JScrollPane(buttonsContainerPanel);
        scrollPane.setPreferredSize(new Dimension(620, 360));
        scrollPane.setAlignmentY(Component.TOP_ALIGNMENT);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Best Solutions"));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JButton closeButton = new JButton("Go Back");
        closeButton.setPreferredSize(new Dimension(150, 75));
        closeButton.addActionListener(e -> parent.showMainPanel());
        bottomPanel.add(closeButton);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void showDestinationResults(String destination) {
        DestinationResultFrame destinationResultFrame = new DestinationResultFrame(destinationResults.get(destination));
        destinationResultFrame.setVisible(true);
    }
}