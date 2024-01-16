package com.aerotrack.console.resultconsole;

import com.aerotrack.console.welcomeconsole.AerotrackApp;
import com.aerotrack.model.entities.Trip;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Map;

public class DestinationsButtonsView extends JFrame{

    private final AerotrackApp parent;
    private final Map<String, List<Trip>> destinationResults;

    public DestinationsButtonsView(AerotrackApp parent, Map<String, List<Trip>> destinationResults) {
        this.parent = parent;
        this.destinationResults = destinationResults;
        initComponents();
    }

    private void initComponents() {
        setLayout(new FlowLayout(FlowLayout.CENTER,10,25));
        setTitle("Choose the Destination");
        setResizable(false);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(0, 3));

        int marginSize = 10;
        getRootPane().setBorder(new EmptyBorder(marginSize, marginSize, marginSize, marginSize));
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
            dispose();
            parent.setVisible(true);
        });
        bottomPanel.add(closeButton);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                parent.setVisible(true);
            }
        });

        add(buttonsPanel);

        add(bottomPanel);


        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();

        setLocationRelativeTo(null);
    }

    private void showDestinationResults(String destination) {
        DestinationResultView destinationResultView = new DestinationResultView(destinationResults.get(destination));
        destinationResultView.setVisible(true);
    }
}