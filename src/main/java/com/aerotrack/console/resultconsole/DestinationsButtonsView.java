package com.aerotrack.console.resultconsole;

import com.aerotrack.model.entities.Trip;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;
import java.util.Map;

public class DestinationsButtonsView extends JFrame{
    private final Map<String, List<Trip>> destinationResults;

    public DestinationsButtonsView(Map<String, List<Trip>> destinationResults) {
        this.destinationResults = destinationResults;
        initComponents();
    }

    private void initComponents() {
        // Layout e configurazione della finestra
        setLayout(new FlowLayout(FlowLayout.LEFT));  // Utilizza un layout di flusso con allineamento a sinistra
        setTitle("Choose the Destination");

        // Pannello contenitore per i bottoni
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(0, 3));  // Imposta il GridLayout con un numero fisso di colonne (3 nel tuo caso)

        // Aggiungi un po' di spazio intorno ai bottoni
        int marginSize = 10;
        getRootPane().setBorder(new EmptyBorder(marginSize, marginSize, marginSize, marginSize));

        // Crea dinamicamente i bottoni per ogni destinazione
        for (String destination : destinationResults.keySet()) {
            JButton destinationButton = new JButton("<html>Flight to " + destination + "<br>Starting from " + destinationResults.get(destination).get(0).getTotalPrice() + "€</html>");

            destinationButton.setPreferredSize(new Dimension(200, 70));
            destinationButton.setMargin(new Insets(10, 10, 10, 10));
            destinationButton.addActionListener(e -> showDestinationResults(destination));
            buttonsPanel.add(destinationButton);
        }

        // Aggiungi il pannello dei bottoni alla finestra principale
        add(buttonsPanel);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();

        setLocationRelativeTo(null);
    }

    private void showDestinationResults(String destination) {
        // Mostra la finestra con i risultati della destinazione corrispondente
        DestinationResultView destinationResultView = new DestinationResultView(destinationResults.get(destination));
        destinationResultView.setVisible(true);
    }
}
