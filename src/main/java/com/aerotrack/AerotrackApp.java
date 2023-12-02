package com.aerotrack;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AerotrackApp extends JFrame {
    private final JTextPane textPane;
    private final List<JTextField> departureFields;
    private final List<JLabel> departureLabels;
    private final JTextField startDateField;
    private final JTextField endDateField;
    private final JTextField durationField;
    private final int baseHeight;


    public AerotrackApp() {
        setTitle("AeroTrack Console App");
        setSize(500,300 );
        baseHeight = getHeight(); // Salva l'altezza iniziale della finestra
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); //console al centro
        setLayout(new BorderLayout());

        textPane = new JTextPane();
        textPane.setEditable(false); // Make it non-editable

        addStyledText("Welcome to the AeroTrack Console App!\n" +
                "Add up to 5 departure airports, and the most convenient flights during\n your chosen period will be shown to you! :)\n\n", null);

        JScrollPane scrollPane = new JScrollPane(textPane);
        add(scrollPane, BorderLayout.NORTH);

        final JPanel inputPanel = new JPanel(new GridLayout(0, 2, 5, 5));

        // Initialize the list of departure fields
        departureFields = new ArrayList<>();
        departureLabels = new ArrayList<>();

        // Add the first departure field
        addDepartureField(inputPanel);

        // Add the other input fields
        JLabel startDateLabel = new JLabel("Start Date (yyyy-MM-dd):");
        startDateField = new JTextField(10);
        inputPanel.add(startDateLabel);
        inputPanel.add(startDateField);

        JLabel endDateLabel = new JLabel("End Date (yyyy-MM-dd):");
        endDateField = new JTextField(10);
        inputPanel.add(endDateLabel);
        inputPanel.add(endDateField);

        JLabel durationLabel = new JLabel("Duration (days):");
        durationField = new JTextField(10);
        inputPanel.add(durationLabel);
        inputPanel.add(durationField);

        add(inputPanel, BorderLayout.CENTER);

        // New button on the left of "Add Airport" button
        final JButton removeAirportButton = new JButton("Remove Airport");
        removeAirportButton.setEnabled(false); // Impostare il bottone come non cliccabile
        removeAirportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add your custom action here
                removeDepartureField(inputPanel);
                if(departureFields.size() == 1){
                    removeAirportButton.setEnabled(false);
                }
                resizeWindow();
            }
        });

        JButton addAirportButton = new JButton("Add Airport");
        addAirportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add a new departure field if there are less than 5
                if (departureFields.size() < 5) {
                    addDepartureField(inputPanel);
                    removeAirportButton.setEnabled(true);
                    validate();
                    repaint();
                    resizeWindow();
                } else {
                    JOptionPane.showMessageDialog(AerotrackApp.this,
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
                submitFlightInfo();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(removeAirportButton);
        buttonPanel.add(addAirportButton);
        buttonPanel.add(submitButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void addDepartureField(JPanel inputPanel) {
        JLabel departureLabel = new JLabel("Departure Airport:");
        JTextField departureField = new JTextField(10);
        departureFields.add(departureField);
        departureLabels.add(departureLabel);

        // Trova l'indice dell'etichetta "Start Date"
        int startDateIndex = -1;
        Component[] components = inputPanel.getComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof JLabel label) {
                if ("Start Date (yyyy-MM-dd):".equals(label.getText())) {
                    startDateIndex = i;
                    break;
                }
            }
        }
        // Inserisci la nuova riga di partenza prima dell'etichetta "Start Date"
        if (startDateIndex != -1) {
            inputPanel.add(departureLabel, startDateIndex);
            inputPanel.add(departureField, startDateIndex + 1);
        } else {
            // Se non trovi "Start Date", aggiungi semplicemente alla fine
            inputPanel.add(departureLabel);
            inputPanel.add(departureField);
        }

        // Aggiorna la visualizzazione
        validate();
        repaint();
    }

    private void removeDepartureField(JPanel inputPanel) {
        int lastIndex = departureFields.size() - 1;
        if (lastIndex >= 0) {
            // Rimuovi l'ultimo campo di partenza e le relative etichette
            inputPanel.remove(departureLabels.get(lastIndex));
            inputPanel.remove(departureFields.get(lastIndex));

            // Rimuovi dai tuoi elenchi locali
            departureLabels.remove(lastIndex);
            departureFields.remove(lastIndex);

            // Aggiorna la visualizzazione
            inputPanel.revalidate();
            inputPanel.repaint();
        }
    }

    private void submitFlightInfo() {
        Set<String> uniqueDepartures = new HashSet<String>();
        boolean isValid = true;

        for (JTextField departureField : departureFields) {
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

        String startDateString = startDateField.getText();
        String endDateString = endDateField.getText();
        String durationString = durationField.getText();

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
            addStyledText("Flight booked from " + getDepartureAirports() +
                    " from " + startDateString + " to " + endDateString + " (Duration: " + durationString + " days)",null);
        } else {
            appendErrorText("Please fill in all fields.");
        }
    }

    private String getDepartureAirports() {
        StringBuilder airports = new StringBuilder();
        for (JTextField departureField : departureFields) {
            String departureAirport = departureField.getText().trim();
            if (!departureAirport.isEmpty()) {
                airports.append(departureAirport).append(", ");
            }
        }
        if (airports.length() > 2) {
            airports.setLength(airports.length() - 2); // Remove the trailing comma and space
        }
        return airports.toString();
    }

    private void appendErrorText(String text) {
        // Imposta il colore del testo a rosso
        SimpleAttributeSet attributes = new SimpleAttributeSet();
        StyleConstants.setForeground(attributes, Color.RED);

        // Aggiunge il testo al JTextPane con gli attributi specificati
        addStyledText(text + "\n", attributes);
    }

    private void addStyledText(String text, AttributeSet attributes) {
        StyledDocument doc = textPane.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), text, attributes);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void resizeWindow() {
        // Imposta la nuova altezza della finestra in base al numero di aeroporti
        int newHeight = baseHeight + departureFields.size() * 30;
        setSize(getWidth(), newHeight);
    }


    //private void appendText(String text) {
    //     textArea.append(text + "\n");
    //}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AerotrackApp();
            }
        });
    }
}
