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
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

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
    private enum AirportCode {
        LHR, CDG, FRA, AMS, MAD, BCN, FCO, MUC, LGW, IST,
        CPH, SVO, DME, ORY, ARN, ZRH, VIE, MAN, ATH, LIS,
        OSL, HEL, DUB, BRU, TXL, MXP, GVA, PRG, WAW, DUS,
        BUD, OTP, MLA, STR, TSF, TRS, HAM, VCE, EIN, STN,
        VLC, NCE, BLQ, NAP, BHX, GLA, LBA, EDI, BRS, SEN;


        public static List<String> toList(){
            return Arrays.stream(AirportCode.values())
                    .map(Enum::toString)
                    .collect(Collectors.toList());
        }
    }


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
        JXComboBox departureAirportsComboBox = new JXComboBox(new Vector<>(AirportCode.toList()));
        AutoCompleteDecorator.decorate(departureAirportsComboBox, ObjectToStringConverter.DEFAULT_IMPLEMENTATION);
        departureAirportsComboBoxes.add(departureAirportsComboBox);
        departureAirportsComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, departureAirportsComboBox.getPreferredSize().height));
        departureAirportsComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        innerDeparturePanel.add(departureAirportsComboBox);

        innerDeparturePanel.revalidate();
        innerDeparturePanel.repaint();
    }

    public void addDestinationBox(JPanel innerDestinationPanel){
        JXComboBox destinationAirportsComboBox = new JXComboBox(new Vector<>(AirportCode.toList()));
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

}
