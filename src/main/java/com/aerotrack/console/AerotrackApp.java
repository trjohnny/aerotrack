// AerotrackConsole costituisce il pannello principale della console
package com.aerotrack.console;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.BorderLayout;
import java.awt.Color;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


public class AerotrackApp extends JFrame {
    private final JTextPane textPane;
    private final FlightInfoFields flightInfoFields;
    private final int baseHeight;

    public AerotrackApp() {
        setTitle("AeroTrack Console App");
        setSize(500, 300);
        baseHeight = getHeight();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        textPane = new JTextPane();
        textPane.setEditable(false);

        addStyledText("""
                Welcome to the AeroTrack Console App!
                Add up to 5 departure airports, and the most convenient flights during
                 your chosen period will be shown to you! :)

                """, null);

        JScrollPane scrollPane = new JScrollPane(textPane);
        add(scrollPane, BorderLayout.NORTH);

        flightInfoFields = new FlightInfoFields();
        InputPanel airportPanel = new InputPanel(this, flightInfoFields);
        add(airportPanel.getPanel(), BorderLayout.CENTER);

        ButtonManager buttonManager = new ButtonManager(this, airportPanel, flightInfoFields);
        add(buttonManager.getPanel(), BorderLayout.SOUTH);

        setVisible(true);
    }

    public void resizeWindow() {
        int newHeight = baseHeight + flightInfoFields.getDepartureFields().size() * 30;
        setSize(getWidth(), newHeight);
    }

    public void addStyledText(String text, AttributeSet attributes) {
        StyledDocument doc = textPane.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), text, attributes);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void appendErrorText(String text) {
        // Imposta il colore del testo a rosso
        SimpleAttributeSet attributes = new SimpleAttributeSet();
        StyleConstants.setForeground(attributes, Color.RED);

        // Aggiunge il testo al JTextPane con gli attributi specificati
        addStyledText(text + "\n", attributes);
    }

    void submitFlightInfo(FlightInfoFields flightInfoFields) {

        Set<String> uniqueDepartures = new HashSet<>();
        boolean isValid = true;

        for (JTextField departureField : flightInfoFields.getDepartureFields()) {
            String departureAirport = departureField.getText().trim();
            if (departureAirport.isEmpty()) {
                continue; // Skip empty fields
            }

            if (!uniqueDepartures.add(departureAirport)) {
                isValid = false;
                JOptionPane.showMessageDialog(this,
                        "Duplicate departure airport: " + departureAirport,
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        String startDateString = flightInfoFields.getStartDateField().getText();
        String endDateString = flightInfoFields.getEndDateField().getText();
        String durationString = flightInfoFields.getDurationField().getText();


        // Validating dates
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date startDate = dateFormat.parse(startDateString);
            Date endDate = dateFormat.parse(endDateString);

            if (endDate.before(startDate)) {
                appendErrorText("Error: End date must be equal or after start date.");
                return;
            }
        } catch (ParseException e) {
            appendErrorText("Error: Invalid date format. Please use yyyy-MM-dd.");
            return;
        }

        if (isValid && !startDateString.isEmpty() && !endDateString.isEmpty() && !durationString.isEmpty()) {
            addStyledText("Flight booked from " + flightInfoFields.getDepartureAirports() +
                    " from " + startDateString + " to " + endDateString + " (Duration: " + durationString + " days)\n", null);
        } else {
            appendErrorText("Please fill in all fields.");
        }
    }
}