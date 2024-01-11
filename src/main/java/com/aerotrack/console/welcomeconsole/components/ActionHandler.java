package com.aerotrack.console.welcomeconsole.components;

import com.aerotrack.model.entities.AerotrackStage;
import com.aerotrack.model.protocol.ScanQueryRequest;
import com.aerotrack.console.resultconsole.ScanOutputView;
import com.aerotrack.console.welcomeconsole.ScanInputView;
import com.aerotrack.model.entities.Trip;

import javax.swing.JTextPane;
import javax.swing.SwingWorker;
import javax.swing.JOptionPane;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.aerotrack.utils.clients.api.AerotrackApiClient;
import lombok.extern.slf4j.Slf4j;
import org.jdesktop.swingx.JXComboBox;

import static com.aerotrack.utils.Utils.appendErrorText;
import static com.aerotrack.utils.Utils.convertDate;


@Slf4j
public class ActionHandler {
    private final InputPanel inputPanel;
    private final AerotrackApiClient aerotrackApiClient;


    public ActionHandler(InputPanel inputPanel){
        this.inputPanel = inputPanel;
        this.aerotrackApiClient = AerotrackApiClient.create(AerotrackStage.ALPHA);
    }


    public void submitFlightInfo(ScanInputView parent, JTextPane textPane) {
        String startDateString;
        String endDateString;
        try {
            startDateString = convertDate(inputPanel.getStartDatePicker().getText());
            endDateString = convertDate(inputPanel.getEndDatePicker().getText());
        } catch (ParseException e){
            JOptionPane.showMessageDialog(textPane,"Insert the dates in then correct way: dd MMMM yyyy", "Format Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String minDurationString = inputPanel.getMinDaysField().getText();
        String maxDurationString = inputPanel.getMaxDaysField().getText();
        List<String> departureAirports = new ArrayList<>();
        for (JXComboBox departureField : inputPanel.getDepartureAirportsComboBoxes()) {
            String departureAirport = (String) departureField.getSelectedItem();
            if (departureAirport != null && !departureAirport.trim().isEmpty()) {
                departureAirports.add(departureAirport.trim());
            }
        }

        List<String> destinationAirports = new ArrayList<>();
        for (JXComboBox destinationField : inputPanel.getDestinationAirportsComboBoxes()) {
            String destinationAirport = (String) destinationField.getSelectedItem();
            if (destinationAirport != null && !destinationAirport.trim().isEmpty()) {
                destinationAirports.add(destinationAirport.trim());
            }
        }

        // Controllo che i campi siano pieni
        if (startDateString.isEmpty() || endDateString.isEmpty() || Objects.isNull(minDurationString) ||
                minDurationString.isEmpty() || Objects.isNull(maxDurationString) || maxDurationString.isEmpty() ||
                departureAirports.isEmpty() || destinationAirports.isEmpty()) {
            JOptionPane.showMessageDialog(textPane,"Please fill in all fields.", "Fields Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Set<String> duplicateDepartures = findDuplicates(departureAirports);
        Set<String> duplicateDestinations = findDuplicates(destinationAirports);

        if (!duplicateDepartures.isEmpty() || !duplicateDestinations.isEmpty()) {
            StringBuilder errorMessageBuilder = new StringBuilder("Error: Duplicate airports detected.\n");

            if (!duplicateDepartures.isEmpty()) {
                String duplicateDeparturesString = String.join(", ", duplicateDepartures);
                errorMessageBuilder.append("Duplicate departure airports: ").append(duplicateDeparturesString).append("\n");
            }

            if (!duplicateDestinations.isEmpty()) {
                String duplicateDestinationsString = String.join(", ", duplicateDestinations);
                errorMessageBuilder.append("Duplicate destination airports: ").append(duplicateDestinationsString);
            }

            JOptionPane.showMessageDialog(textPane, errorMessageBuilder.toString(), "Duplicate Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Controlli date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            LocalDate currentDate = LocalDate.now();
            LocalTime midnight = LocalTime.MIDNIGHT;
            LocalDateTime todayMidnight = LocalDateTime.of(currentDate, midnight);

            Date today = java.sql.Timestamp.valueOf(todayMidnight);


            Date startDate = dateFormat.parse(startDateString);
            System.out.println(today +" " + startDate);
            Date endDate = dateFormat.parse(endDateString);
            int minDuration = Integer.parseInt(minDurationString);
            int maxDuration = Integer.parseInt(maxDurationString);

            // Controllo che la data di partenza sia maggiore di quella attuale
            if (startDate.before(today) || endDate.before(today)) {
                appendErrorText("Error: Dates must be equal or after the today's date.",textPane);
                JOptionPane.showMessageDialog(textPane, "Error: Dates must be equal or after the today's date.", "Date Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Controllo che la end date sia successiva alla start date
            if (endDate.before(startDate)) {
                JOptionPane.showMessageDialog(textPane, "Error: End date must be equal or after start date.", "Date Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Controllo che min duration sia minore o uguale a max duration
            if (minDuration > maxDuration) {
                JOptionPane.showMessageDialog(textPane, "Error: Min duration must be less than or equal to max duration.", "Duration Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Calcolo la differenza tra end date e start date in giorni
            long duration = (endDate.getTime() - startDate.getTime()) / (24 * 60 * 60 * 1000);

            // Controllo che la differenza tra end date e start date sia maggiore o uguale a max duration
            if (duration < maxDuration) {
                JOptionPane.showMessageDialog(textPane, "Error: End date must be at least " + maxDuration + " days after start date.", "Duration Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

        } catch (ParseException | NumberFormatException e) {
            appendErrorText("Error: Invalid date or duration format. Please use yyyy-MM-dd for dates and numerical values for duration.",textPane);
            return;
        }

        ScanQueryRequest scanQueryRequest = buildScanQueryRequest(minDurationString, maxDurationString, startDateString, endDateString, departureAirports, inputPanel.getReturnToSameAirportCheckBox().isSelected(), destinationAirports);
        SwingWorker<List<Trip>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Trip> doInBackground() {
                return aerotrackApiClient.getBestFlight(scanQueryRequest);
            }

            @Override
            protected void done() {
                try {
                    List<Trip> tripList = get();

                    if (tripList != null) {
                        // La chiamata è andata a buon fine, puoi gestire la risposta qui
                        // Ad esempio, puoi visualizzare i risultati nella console dei risultati
                        ScanOutputView scanOutputView = new ScanOutputView(tripList);
                    } else {
                        // La chiamata ha restituito una risposta nulla, gestisci l'errore
                        appendErrorText("Error: Failed to get valid response from API.", textPane);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // Gestisci l'eccezione
                    appendErrorText("Error: An exception occurred during API call.", textPane);
                }
            }
        };
        // Avvia il lavoro in background
        worker.execute();
    }

    public ScanQueryRequest buildScanQueryRequest(String minDurationString, String maxDurationString,String startDateString, String endDateString, List<String> departureAirports, Boolean returnToSameAirport, List<String> destinationAirports) {
        ScanQueryRequest.ScanQueryRequestBuilder builder = ScanQueryRequest.builder();

        builder.minDays(Integer.valueOf(minDurationString));
        builder.maxDays(Integer.valueOf(maxDurationString));
        builder.availabilityStart(startDateString);
        builder.availabilityEnd(endDateString);
        builder.departureAirports(departureAirports);
        builder.destinationAirports(destinationAirports);
        builder.returnToSameAirport(returnToSameAirport);
        return builder.build();
    }

    public static Set<String> findDuplicates(Collection<String> airports) {
        Set<String> uniqueAirports = new HashSet<>();
        return airports.stream()
                .filter(airport -> !uniqueAirports.add(airport))
                .collect(Collectors.toSet());
    }
}
