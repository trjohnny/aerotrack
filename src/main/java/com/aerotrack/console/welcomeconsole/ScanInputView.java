// AerotrackConsole costituisce il pannello principale della console
package com.aerotrack.console.welcomeconsole;

import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;

import com.aerotrack.console.welcomeconsole.components.ButtonManager;
import com.aerotrack.console.welcomeconsole.components.FlightInfoFields;
import com.aerotrack.console.welcomeconsole.components.InputPanel;
import com.aerotrack.utils.ResourceHelper;

import static com.aerotrack.utils.Utils.addStyledText;



public class ScanInputView extends JFrame {
    private final FlightInfoFields flightInfoFields;
    private final int baseHeight = 340;

    public ScanInputView() {
        setTitle(ResourceHelper.getString("title"));
        setSize(500, baseHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);

        addStyledText(ResourceHelper.getString("welcomeMessage"), null, textPane);

        JScrollPane scrollPane = new JScrollPane(textPane);
        add(scrollPane, BorderLayout.NORTH);

        flightInfoFields = new FlightInfoFields();
        InputPanel inputPanel = new InputPanel(this, flightInfoFields);
        add(inputPanel.getPanel(), BorderLayout.CENTER);

        ButtonManager buttonManager = new ButtonManager(this, inputPanel, flightInfoFields, textPane);
        add(buttonManager.getPanel(), BorderLayout.SOUTH);

        setVisible(true);
    }

    public void resizeWindow() {
        int newHeight = baseHeight + flightInfoFields.getDepartureFields().size() * 30;
        setSize(getWidth(), newHeight);
    }
}