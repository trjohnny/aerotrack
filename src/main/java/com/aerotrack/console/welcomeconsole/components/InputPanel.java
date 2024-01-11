// InputPanel contiene il codice per la costruzione del riquadro degli input
package com.aerotrack.console.welcomeconsole.components;

import com.aerotrack.console.welcomeconsole.ScanInputView;
import com.github.lgooddatepicker.components.DatePicker;
import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.autocomplete.ObjectToStringConverter;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import lombok.Getter;


@Getter
public class InputPanel {
    private final JPanel panel;
    private final ScanInputView parent;
    private final DatePicker startDatePicker;
    private final DatePicker endDatePicker;

    private final JTextField minDaysField;
    private final JTextField maxDaysField;
    private final JCheckBox returnToSameAirportCheckBox;
    private final List<JXComboBox> departureAirportsComboBoxes;
    private final List<JXComboBox> destinationAirportsComboBoxes;
    private final JPanel innerDeparturePanel;
    private final JPanel innerDestinationPanel;




    public InputPanel(ScanInputView parent) {
        this.parent = parent;
        startDatePicker = new DatePicker();
        endDatePicker = new DatePicker();
        minDaysField = new JTextField(10);
        minDaysField.setText("1");
        maxDaysField = new JTextField(10);
        maxDaysField.setText("15");
        returnToSameAirportCheckBox = new JCheckBox("Return to the same airport", false);
        departureAirportsComboBoxes = new ArrayList<>();
        destinationAirportsComboBoxes = new ArrayList<>();

        panel = new JPanel(new GridLayout(0, 2, 5, 5));

        innerDeparturePanel = new JPanel(new GridLayout(0, 1));
        innerDestinationPanel = new JPanel(new GridLayout(0, 1));
        innerDeparturePanel.setLayout(new BoxLayout(innerDeparturePanel, BoxLayout.Y_AXIS));
        innerDestinationPanel.setLayout(new BoxLayout(innerDestinationPanel, BoxLayout.Y_AXIS));

        JLabel startDateLabel = new JLabel("Start Date (yyyy-MM-dd):");
        JLabel endDateLabel = new JLabel("End Date (yyyy-MM-dd):");
        JLabel minDaysDurationLabel = new JLabel("Min duration of the trip (days):");
        JLabel maxDaysDurationLabel = new JLabel("Max duration of the trip  (days):");
        JLabel departureAirportsLabel = new JLabel("Departure Airports:");
        JLabel destinationAirportsLabel = new JLabel("Destination Airports:");

        panel.add(startDateLabel);
        panel.add(endDateLabel);
        panel.add(startDatePicker);
        panel.add(endDatePicker);
        panel.add(minDaysDurationLabel);
        panel.add(maxDaysDurationLabel);
        panel.add(minDaysField);
        panel.add(maxDaysField);

        panel.add(returnToSameAirportCheckBox);

        JPanel innerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        innerPanel.add(departureAirportsLabel);
        innerPanel.add(destinationAirportsLabel);
        panel.add(innerPanel);

        panel.add(departureAirportsLabel);
        panel.add(destinationAirportsLabel);

        addDepartureBox(innerDeparturePanel);
        addDestinationBox(innerDestinationPanel);
        panel.add(innerDeparturePanel);
        panel.add(innerDestinationPanel);

    }


    public void addDepartureBox(JPanel innerDeparturePanel){
        JXComboBox departureAirportsComboBox = new JXComboBox(new Vector<>(List.of("VCE","DUB","TSF","TRS","STN")));
        AutoCompleteDecorator.decorate(departureAirportsComboBox, ObjectToStringConverter.DEFAULT_IMPLEMENTATION);
        departureAirportsComboBoxes.add(departureAirportsComboBox);
        innerDeparturePanel.add(departureAirportsComboBox);
        resizeComboBoxesPanels();
    }

    public void addDestinationBox(JPanel innerDestinationPanel){
        JXComboBox destinationAirportsComboBox = new JXComboBox(new Vector<>(List.of("VCE","DUB","TSF","TRS","STN")));
        AutoCompleteDecorator.decorate(destinationAirportsComboBox, ObjectToStringConverter.DEFAULT_IMPLEMENTATION);
        destinationAirportsComboBoxes.add(destinationAirportsComboBox);
        innerDestinationPanel.add(destinationAirportsComboBox);
        resizeComboBoxesPanels();
    }

    public void removeDepartureBox(JPanel innerDeparturePanel){
        int lastIndex = departureAirportsComboBoxes.size()-1;
        if(lastIndex >= 0){
            innerDeparturePanel.remove(lastIndex);
            departureAirportsComboBoxes.remove(lastIndex);

            panel.revalidate();
            panel.repaint();
        }
    }

    public void removeDestinationBox(JPanel innerDestinationPanel){
        int lastIndex = destinationAirportsComboBoxes.size()-1;
        if(lastIndex >= 0){
            innerDestinationPanel.remove(lastIndex);
            destinationAirportsComboBoxes.remove(lastIndex);

            panel.revalidate();
            panel.repaint();
        }
    }

    public void resizeComboBoxesPanels() {
        int maxSize = Math.max(departureAirportsComboBoxes.size(), destinationAirportsComboBoxes.size());
        int newHeight = maxSize * 40;

        // Calcola la nuova altezza basandoti sulla dimensione attuale e il numero di elementi
        int departurePanelHeight = innerDeparturePanel.getPreferredSize().height;
        int destinationPanelHeight = innerDestinationPanel.getPreferredSize().height;

        // Imposta la dimensione minima dei pannelli interni
        innerDeparturePanel.setMinimumSize(new Dimension(innerDeparturePanel.getWidth(), departurePanelHeight + newHeight));
        innerDestinationPanel.setMinimumSize(new Dimension(innerDestinationPanel.getWidth(), destinationPanelHeight + newHeight));

        //Ridimensiona anche il pannello più grande
        parent.getScanInputViewTextPane().setSize(900,parent.getScanInputViewTextPane().getHeight()+newHeight);

        panel.revalidate();
        panel.repaint();

        parent.revalidate();
        parent.repaint();
    }

}
