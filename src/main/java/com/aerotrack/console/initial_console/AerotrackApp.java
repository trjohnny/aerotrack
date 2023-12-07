// AerotrackConsole costituisce il pannello principale della console
package com.aerotrack.console.initial_console;

import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;


import com.aerotrack.client.ApiGatewayClient;
import com.aerotrack.console.initial_console.component.ButtonManager;
import com.aerotrack.console.initial_console.component.FlightInfoFields;
import com.aerotrack.console.initial_console.component.InputPanel;

import static com.aerotrack.utils.Utils.addStyledText;


public class AerotrackApp extends JFrame {
    private final FlightInfoFields flightInfoFields;
    private final int baseHeight;

    public AerotrackApp() {
        setTitle("AeroTrack Console App");
        setSize(500, 340);
        ApiGatewayClient apiGatewayClient = new ApiGatewayClient("https://f1muce19kh.execute-api.eu-west-1.amazonaws.com/prod/scan", "z9inDLaWtOamHqvOCl25w33KtSbqVpOf61oPHGhK");
        baseHeight = getHeight();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);

        addStyledText("""
                Welcome to the AeroTrack Console App!
                Add up to 5 departure airports, and the most convenient flights during
                 your chosen period will be shown to you! :)

                """, null, textPane);

        JScrollPane scrollPane = new JScrollPane(textPane);
        add(scrollPane, BorderLayout.NORTH);

        flightInfoFields = new FlightInfoFields();
        InputPanel inputPanel = new InputPanel(this, flightInfoFields);
        add(inputPanel.getPanel(), BorderLayout.CENTER);

        ButtonManager buttonManager = new ButtonManager(this, inputPanel, flightInfoFields, apiGatewayClient, textPane);
        add(buttonManager.getPanel(), BorderLayout.SOUTH);

        setVisible(true);
    }

    public void resizeWindow() {
        int newHeight = baseHeight + flightInfoFields.getDepartureFields().size() * 30;
        setSize(getWidth(), newHeight);
    }
}