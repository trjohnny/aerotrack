package com.aerotrack.console.resultconsole;

import com.aerotrack.model.entities.Flight;
import com.aerotrack.model.entities.Trip;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class DestinationResultView extends JFrame {
    private final List<Trip> destinationTrips;
    private static int tripCounter = 0;
    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy, HH:mm");

    public DestinationResultView(List<Trip> destinationTrips) {
        this.destinationTrips = destinationTrips;
        initComponents();
    }

    private void initComponents() {
        setTitle("Destination Results Console");
        tripCounter = 0;
        setSize(900, 600); // Adjust the size as needed
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);

        for (Trip trip : destinationTrips) {
            JPanel tripPanel = new
                    JPanel();
            tripPanel.setLayout(new BorderLayout());
            tripPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            tripPanel.setBackground(Color.WHITE);
            // Increment trip counter for display
            tripCounter++;

            // Create header panel for trip number and total price
            JPanel headerPanel = new JPanel();
            headerPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            JLabel tripLabel = new JLabel("Trip " + tripCounter + " - Total Price: €" + trip.getTotalPrice());
            tripLabel.setFont(new Font("Arial", Font.BOLD, 16));
            headerPanel.add(tripLabel);
            tripPanel.add(headerPanel, BorderLayout.NORTH);

            // Create a sub-panel to hold both outbound and return flight panels side by side
            JPanel flightsPanel = new JPanel(new GridLayout(1, 2, 10, 0)); // 1 row, 2 columns, 10px horizontal gap
            flightsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            flightsPanel.setBackground(Color.WHITE);

            JPanel outboundPanel = createFlightPanel(trip.getOutboundFlights().get(0), "Outbound Flights");
            JPanel returnPanel = createFlightPanel(trip.getReturnFlights().get(0), "Return Flights");

            flightsPanel.add(outboundPanel);
            flightsPanel.add(returnPanel);

            tripPanel.add(flightsPanel, BorderLayout.CENTER);

            mainPanel.add(tripPanel);
            mainPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add space between cards
        }

        // Set the scroll pane to start at the top when the view is displayed
        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0));

        setVisible(true);
    }

    private JPanel createFlightPanel(Flight flight, String title) {
        JPanel flightPanel = new JPanel();
        flightPanel.setLayout(new BoxLayout(flightPanel, BoxLayout.Y_AXIS));
        flightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        flightPanel.add(titleLabel);
        JLabel directionLabel = new JLabel("Flight: " + flight.getDirection());
        JLabel departureLabel = new JLabel("Departure: " + dateFormat.format(parseIso8601Date(flight.getDepartureDateTime())));
        JLabel arrivalLabel = new JLabel("Arrival: " + dateFormat.format(parseIso8601Date(flight.getArrivalDateTime())));
        JLabel flightNumberLabel = new JLabel("Flight Number: " + flight.getFlightNumber());
        JLabel priceLabel = new JLabel("Price: $" + flight.getPrice());

        flightPanel.add(directionLabel);
        flightPanel.add(departureLabel);
        flightPanel.add(arrivalLabel);
        flightPanel.add(flightNumberLabel);
        flightPanel.add(priceLabel);

        return flightPanel;
    }

    private Date parseIso8601Date(String isoDate) {
        SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        iso8601Format.setTimeZone(TimeZone.getTimeZone("UTC")); // ISO 8601 is generally in UTC
        try {
            return iso8601Format.parse(isoDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
