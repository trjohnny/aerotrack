package com.aerotrack.console.welcomeconsole.components;

import com.aerotrack.console.welcomeconsole.AerotrackApp;
import com.aerotrack.model.entities.AirportsJsonFile;
import com.github.lgooddatepicker.components.DatePicker;
import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.autocomplete.ObjectToStringConverter;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import lombok.Getter;

import static com.aerotrack.utils.Utils.countryNameToCodeMap;


@Getter
public class InputPanel {
    private final JPanel panel;
    private final AerotrackApp parent;
    private final DatePicker startDatePicker;
    private final DatePicker endDatePicker;
    private final JTextField minDaysField;
    private final JTextField maxDaysField;
    private final JCheckBox returnToSameAirportCheckBox;
    private final List<JXComboBox> departureAirportsComboBoxes;
    private final List<JXComboBox> destinationAirportsComboBoxes;
    private final JPanel innerDestinationPanel;
    private final JPanel innerDeparturePanel;

    private final JScrollPane departureScrollPane;
    private final JScrollPane destinationScrollPane;
    public final AirportsJsonFile airportsJsonFile;


    public InputPanel(AerotrackApp parent) {
        this.parent = parent;
        airportsJsonFile = parent.getAerotrackApiClient().getAirportsJson();

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        Dimension inputSize = new Dimension(450, 20);
        Dimension labelSize = new Dimension(450, 20);

        startDatePicker = new DatePicker();
        endDatePicker = new DatePicker();
        startDatePicker.getComponentDateTextField().setMaximumSize(inputSize);
        endDatePicker.getComponentDateTextField().setMaximumSize(inputSize);

        minDaysField = new JTextField();
        maxDaysField = new JTextField();
        minDaysField.setMaximumSize(inputSize);
        maxDaysField.setMaximumSize(inputSize);

        returnToSameAirportCheckBox = new JCheckBox("Return to the same airport", true);
        departureAirportsComboBoxes = new ArrayList<>();
        destinationAirportsComboBoxes = new ArrayList<>();

        JPanel datePanel = new JPanel(new GridLayout(2, 2, 5, 5));
        JLabel startDateLabel = new JLabel("Start Date (yyyy-MM-dd):");
        startDateLabel.setPreferredSize(labelSize);
        datePanel.add(startDateLabel);
        datePanel.add(startDatePicker);
        JLabel endDateLabel = new JLabel("End Date (yyyy-MM-dd):");
        endDateLabel.setPreferredSize(labelSize);
        datePanel.add(endDateLabel);
        datePanel.add(endDatePicker);
        JLabel minDurationLabel = new JLabel("Min duration of the trip (days):");
        minDurationLabel.setPreferredSize(labelSize);
        datePanel.add(minDurationLabel);
        datePanel.add(minDaysField);
        JLabel maxDurationLabel = new JLabel("Max duration of the trip (days):");
        maxDurationLabel.setPreferredSize(labelSize);
        datePanel.add(maxDurationLabel);
        datePanel.add(maxDaysField);

        datePanel.setMaximumSize(datePanel.getPreferredSize());
        panel.add(datePanel);

        JPanel checkBoxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        checkBoxPanel.add(returnToSameAirportCheckBox);
        panel.add(checkBoxPanel);

        innerDestinationPanel = new JPanel();
        innerDestinationPanel.setLayout(new BoxLayout(innerDestinationPanel, BoxLayout.Y_AXIS));
        innerDeparturePanel = new JPanel();
        innerDeparturePanel.setLayout(new BoxLayout(innerDeparturePanel, BoxLayout.Y_AXIS));
        innerDestinationPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        innerDeparturePanel.setAlignmentY(Component.TOP_ALIGNMENT);

        departureScrollPane = new JScrollPane(innerDeparturePanel);
        departureScrollPane.setPreferredSize(new Dimension(450, 1000));
        departureScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        departureScrollPane.setAlignmentY(Component.TOP_ALIGNMENT);

        destinationScrollPane = new JScrollPane(innerDestinationPanel);
        destinationScrollPane.setPreferredSize(new Dimension(450, 1000));
        destinationScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        destinationScrollPane.setAlignmentY(Component.TOP_ALIGNMENT);
        departureScrollPane.setBorder(BorderFactory.createTitledBorder("Departure Airports"));
        destinationScrollPane.setBorder(BorderFactory.createTitledBorder("Destinations"));

        JPanel scrollPaneContainer = new JPanel();
        scrollPaneContainer.setLayout(new BoxLayout(scrollPaneContainer, BoxLayout.LINE_AXIS));
        scrollPaneContainer.add(departureScrollPane);
        scrollPaneContainer.add(destinationScrollPane);
        panel.add(scrollPaneContainer);

        panel.revalidate();
        panel.repaint();
    }

    public JXComboBox getComboBox(List<String> additionalValues, JPanel airportsPanel){
        List<String> flights = new ArrayList<>(airportsJsonFile.getAirports()
                .stream()
                .map(airport -> airport.getName() + " [" + airport.getAirportCode() + "]")
                .sorted()
                .toList());

        additionalValues.addAll(flights);

        JXComboBox airportsComboBox = new JXComboBox(new Vector<>(additionalValues));
        AutoCompleteDecorator.decorate(airportsComboBox, ObjectToStringConverter.DEFAULT_IMPLEMENTATION);

        airportsComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, airportsComboBox.getPreferredSize().height));
        airportsComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        airportsPanel.add(airportsComboBox);
        return airportsComboBox;
    }

    public void addDepartureBox(JPanel innerDeparturePanel){
        List<String> emptyList = new ArrayList<>();
        departureAirportsComboBoxes.add(getComboBox(emptyList, innerDeparturePanel));
        innerDeparturePanel.revalidate();
        innerDeparturePanel.repaint();
    }

    public void addDestinationBox(JPanel innerDestinationPanel){
        List<String> countries = new ArrayList<>(countryNameToCodeMap.keySet().stream().sorted().toList());

        destinationAirportsComboBoxes.add(getComboBox(countries, innerDestinationPanel));
        innerDestinationPanel.revalidate();
        innerDestinationPanel.repaint();
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

}
