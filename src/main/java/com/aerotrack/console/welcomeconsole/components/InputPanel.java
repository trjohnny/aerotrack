// InputPanel contiene il codice per la costruzione del riquadro degli input
package com.aerotrack.console.welcomeconsole.components;

import com.aerotrack.console.welcomeconsole.ScanInputView;
import com.github.lgooddatepicker.components.DatePicker;
import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.autocomplete.ObjectToStringConverter;

import javax.swing.*;
import java.awt.*;
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

    private final JScrollPane departureScrollPane;
    private final JScrollPane destinationScrollPane;


    public InputPanel(ScanInputView parent) {
        this.parent = parent;
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        startDatePicker = new DatePicker();
        endDatePicker = new DatePicker();
        minDaysField = new JTextField("1", 10);
        maxDaysField = new JTextField("15", 10);
        returnToSameAirportCheckBox = new JCheckBox("Return to the same airport", false);
        departureAirportsComboBoxes = new ArrayList<>();
        destinationAirportsComboBoxes = new ArrayList<>();



        // Set fixed size for the input fields
        Dimension inputSize = new Dimension(450, 20);
        Dimension labelSize = new Dimension(450, 20);
        startDatePicker.getComponentDateTextField().setMaximumSize(inputSize);
        endDatePicker.getComponentDateTextField().setMaximumSize(inputSize);
        minDaysField.setMaximumSize(inputSize);
        maxDaysField.setMaximumSize(inputSize);

        // Create the date panel
        JPanel datePanel = new JPanel(new GridLayout(2, 2, 5, 5));

        // Prima colonna
        JLabel startDateLabel = new JLabel("Start Date (yyyy-MM-dd):");
        startDateLabel.setPreferredSize(labelSize);
        datePanel.add(startDateLabel);
        datePanel.add(startDatePicker);
        JLabel endDateLabel = new JLabel("End Date (yyyy-MM-dd):");
        endDateLabel.setPreferredSize(labelSize);
        datePanel.add(endDateLabel);
        datePanel.add(endDatePicker);

        // Seconda colonna: Min duration e Max duration
        JLabel minDurationLabel = new JLabel("Min duration of the trip (days):");
        minDurationLabel.setPreferredSize(labelSize);
        datePanel.add(minDurationLabel);
        datePanel.add(minDaysField);
        JLabel maxDurationLabel = new JLabel("Max duration of the trip (days):");
        maxDurationLabel.setPreferredSize(labelSize);
        datePanel.add(maxDurationLabel);
        datePanel.add(maxDaysField);

        // Aggiungi il datePanel al pannello principale
        datePanel.setMaximumSize(datePanel.getPreferredSize());
        panel.add(datePanel);

        // Aggiungi il checkBox allineato a sinistra
        JPanel checkBoxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        checkBoxPanel.add(returnToSameAirportCheckBox);
        panel.add(checkBoxPanel);



        innerDeparturePanel = new JPanel();
        innerDeparturePanel.setLayout(new BoxLayout(innerDeparturePanel, BoxLayout.Y_AXIS));
        innerDestinationPanel = new JPanel();
        innerDestinationPanel.setLayout(new BoxLayout(innerDestinationPanel, BoxLayout.Y_AXIS));
        innerDeparturePanel.setAlignmentY(Component.TOP_ALIGNMENT);
        innerDestinationPanel.setAlignmentY(Component.TOP_ALIGNMENT);

        // Initialize and add scroll panes for the inner panels
        departureScrollPane = new JScrollPane(innerDeparturePanel);
        departureScrollPane.setPreferredSize(new Dimension(450, 1000));
        departureScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        departureScrollPane.setAlignmentY(Component.TOP_ALIGNMENT);

        destinationScrollPane = new JScrollPane(innerDestinationPanel);
        destinationScrollPane.setPreferredSize(new Dimension(450, 1000));
        destinationScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        destinationScrollPane.setAlignmentY(Component.TOP_ALIGNMENT);

        // Create a container panel for scroll panes to put them in the same line
        JPanel scrollPaneContainer = new JPanel();
        scrollPaneContainer.setLayout(new BoxLayout(scrollPaneContainer, BoxLayout.LINE_AXIS));
        scrollPaneContainer.add(departureScrollPane);
        scrollPaneContainer.add(destinationScrollPane);
        panel.add(scrollPaneContainer);

        // Optionally, add borders around the scroll panes for visual separation
        departureScrollPane.setBorder(BorderFactory.createTitledBorder("Departure Airports"));
        destinationScrollPane.setBorder(BorderFactory.createTitledBorder("Destinations"));

        // Remember to revalidate and repaint the panel when adding or removing components
        panel.revalidate();
        panel.repaint();
    }


    public void addDepartureBox(JPanel innerDeparturePanel){
        JXComboBox departureAirportsComboBox = new JXComboBox(new Vector<>(List.of("VCE","DUB","TSF","TRS","STN")));
        AutoCompleteDecorator.decorate(departureAirportsComboBox, ObjectToStringConverter.DEFAULT_IMPLEMENTATION);
        departureAirportsComboBoxes.add(departureAirportsComboBox);
        departureAirportsComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, departureAirportsComboBox.getPreferredSize().height));
        departureAirportsComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        innerDeparturePanel.add(departureAirportsComboBox);

        innerDeparturePanel.revalidate();
        innerDeparturePanel.repaint();
    }

    public void addDestinationBox(JPanel innerDestinationPanel){
        JXComboBox destinationAirportsComboBox = new JXComboBox(new Vector<>(List.of("VCE","DUB","TSF","TRS","STN")));
        AutoCompleteDecorator.decorate(destinationAirportsComboBox, ObjectToStringConverter.DEFAULT_IMPLEMENTATION);
        destinationAirportsComboBoxes.add(destinationAirportsComboBox);
        destinationAirportsComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, destinationAirportsComboBox.getPreferredSize().height));
        destinationAirportsComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        innerDestinationPanel.add(destinationAirportsComboBox);

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

    public void resizeComboBoxesPanels() {
        // Calculate new height based on the number of elements but do not change the width
        int maxSize = Math.max(departureAirportsComboBoxes.size(), destinationAirportsComboBoxes.size());
        int newHeight = maxSize * 40; // Assuming each combo box takes 40 pixels in height

        // Set the preferred size of the inner panels to be dynamic only in height
        innerDeparturePanel.setPreferredSize(new Dimension(innerDeparturePanel.getPreferredSize().width, newHeight));
        innerDestinationPanel.setPreferredSize(new Dimension(innerDestinationPanel.getPreferredSize().width, newHeight));

        // Adjust the parent container to accommodate the new height
        parent.getScanInputViewTextPane().setSize(parent.getScanInputViewTextPane().getWidth(), parent.getScanInputViewTextPane().getHeight() + newHeight);

        panel.revalidate();
        panel.repaint();

        parent.revalidate();
        parent.repaint();
    }

}
