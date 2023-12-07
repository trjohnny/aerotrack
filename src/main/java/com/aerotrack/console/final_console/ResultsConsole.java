package com.aerotrack.console.final_console;

import com.aerotrack.model.Flight;
import com.aerotrack.model.FlightPair;

import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.util.List;

import static com.aerotrack.utils.Utils.addStyledText;

public class ResultsConsole extends JFrame {

    private final JTextPane textPane;

    public ResultsConsole(List<FlightPair> flightPairs) {
        setTitle("Results Console");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        textPane = new JTextPane();
        textPane.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textPane);
        add(scrollPane, BorderLayout.CENTER);

        addStyledText("""
                Here are the best flight options tailored to your preferences :)

                """, null, textPane);

        displayFlightPairs(flightPairs);

        setVisible(true);
    }

    private void displayFlightPairs(List<FlightPair> flightPairs) {
        if (flightPairs != null && !flightPairs.isEmpty()) {
            addStyledText("Flight Pairs Information:\n", null, textPane);

            for (FlightPair flightPair : flightPairs) {
                displayFlightInfo("Outbound Flight", flightPair.getOutboundFlight());
                displayFlightInfo("Return Flight", flightPair.getReturnFlight());
            }
        } else {
            addStyledText("No flight pairs found.\n", null, textPane);
        }
    }

    private void displayFlightInfo(String title, Flight flight) {
        addStyledText(title + ":\n", null, textPane);
        addStyledText("Direction: " + flight.getDirection() + "\n", null, textPane);
        addStyledText("Departure DateTime: " + flight.getDepartureDateTime() + "\n", null, textPane);
        addStyledText("Arrival DateTime: " + flight.getArrivalDateTime() + "\n", null, textPane);
        addStyledText("Flight Number: " + flight.getFlightNumber() + "\n", null, textPane);
        addStyledText("Price: " + flight.getPrice() + "\n\n", null, textPane);
    }

}

