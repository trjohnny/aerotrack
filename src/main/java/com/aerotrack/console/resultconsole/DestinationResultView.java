package com.aerotrack.console.resultconsole;

import com.aerotrack.model.entities.Flight;
import com.aerotrack.model.entities.Trip;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.List;

public class DestinationResultView extends JFrame {
    private final List<Trip> destinationTrips;

    public DestinationResultView(List<Trip> destinationTrips) {
        this.destinationTrips = destinationTrips;
        initComponents();
    }

    private void initComponents() {
        setTitle("Destination Results Console");
        setSize(900, 420);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTable table = createResultsTable();
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private JTable createResultsTable() {
        String[] columnNames = {"Outbound Flights", "Return Flights", "Prices"};
        Object[][] rowData = new Object[destinationTrips.size()][3];

        for (int i = 0; i < destinationTrips.size(); i++) {
            Trip trip = destinationTrips.get(i);

            if (!trip.getOutboundFlights().isEmpty() && !trip.getReturnFlights().isEmpty()) {
                rowData[i][0] = formatFlightInfo(trip.getOutboundFlights().get(0));
                rowData[i][1] = formatFlightInfo(trip.getReturnFlights().get(0));
                rowData[i][2] = trip.getTotalPrice().toString();
            } else {
                rowData[i][0] = "N/A";
                rowData[i][1] = "N/A";
                rowData[i][2] = "N/A";
            }
        }

        JTable table = new JTable(rowData, columnNames);
        table.getColumnModel().getColumn(0).setCellRenderer(new MultiLineTableCellRenderer());
        table.getColumnModel().getColumn(1).setCellRenderer(new MultiLineTableCellRenderer());
        table.setRowHeight(100);
        table.getColumnModel().getColumn(0).setPreferredWidth(185);
        table.getColumnModel().getColumn(1).setPreferredWidth(185);
        table.getColumnModel().getColumn(2).setPreferredWidth(30);

        return table;
    }

    private String formatFlightInfo(Flight flight) {
        return "Direction: " + flight.getDirection() + "\n" +
                "Departure DateTime: " + flight.getDepartureDateTime() + "\n" +
                "Arrival DateTime: " + flight.getArrivalDateTime() + "\n" +
                "Flight Number: " + flight.getFlightNumber() + "\n" +
                "Price: " + flight.getPrice() + "\n\n";
    }

    private static class MultiLineTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            label.setText("<html>" + value.toString().replace("\n", "<br/>") + "</html>");
            return label;
        }
    }
}

