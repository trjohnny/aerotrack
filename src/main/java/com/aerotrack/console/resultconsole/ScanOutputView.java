package com.aerotrack.console.resultconsole;

import com.aerotrack.model.entities.Flight;
import com.aerotrack.model.entities.Trip;
import com.aerotrack.utils.ResourceHelper;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Component;

import static com.aerotrack.utils.Utils.addStyledText;

public class ScanOutputView extends JFrame {

    private final JTextPane textPane;

   public ScanOutputView(List<Trip> tripList) {
        setTitle("Results Console");
        setSize(900, 420);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        textPane = new JTextPane();
        textPane.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textPane);
        add(scrollPane, BorderLayout.CENTER);

        addStyledText(ResourceHelper.getString("resultMessage"), null, textPane);

        displayFlightPairs(tripList);

        setVisible(true);
    }

    private void displayFlightPairs(List<Trip> tripList) {
        if (tripList != null && !tripList.isEmpty()) {
            addStyledText("--------------- Results ---------------\n", null, textPane);

            String[] columnNames = {"Outbound Flights", "Return Flights", "Prices"};
            Object[][] rowData = new Object[tripList.size()][3];

            for (int i = 0; i < tripList.size(); i++) {
                Flight outboundFlight = tripList.get(i).getOutboundFlights().get(0);
                Flight returnFlight = tripList.get(i).getReturnFlights().get(0);

                rowData[i][0] = formatFlightInfo(outboundFlight);
                rowData[i][1] = formatFlightInfo(returnFlight);
                rowData[i][2] = tripList.get(i).getTotalPrice().toString();
            }

            JTable table = new JTable(rowData, columnNames);
            table.getColumnModel().getColumn(0).setCellRenderer(new MultiLineTableCellRenderer());
            table.getColumnModel().getColumn(1).setCellRenderer(new MultiLineTableCellRenderer());
            table.setRowHeight(100);
            table.getColumnModel().getColumn(0).setPreferredWidth(185);
            table.getColumnModel().getColumn(1).setPreferredWidth(185);
            table.getColumnModel().getColumn(2).setPreferredWidth(30);
            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);

        } else {
            addStyledText("No flight pairs found.\n", null, textPane);
        }
    }


    private String formatFlightInfo(Flight flight) {
        String formattedInfo = "Direction: " + flight.getDirection() + "\n" +
                "Departure DateTime: " + flight.getDepartureDateTime() + "\n" +
                "Arrival DateTime: " + flight.getArrivalDateTime() + "\n" +
                "Flight Number: " + flight.getFlightNumber() + "\n" +
                "Price: " + flight.getPrice() + "\n\n";
        return formattedInfo;
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

