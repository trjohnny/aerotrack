// AerotrackConsole costituisce il pannello principale della console
package com.aerotrack.console.welcomeconsole;

import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;

import com.aerotrack.console.welcomeconsole.components.ButtonManager;
import com.aerotrack.console.welcomeconsole.components.InputPanel;
import com.aerotrack.utils.ResourceHelper;

import static com.aerotrack.utils.Utils.addStyledText;


public class ScanInputView extends JFrame {

    private final JTextPane textPane;
    public int baseHeight = 420;

    public ScanInputView() {
        setTitle(ResourceHelper.getString("title"));
        setSize(900, baseHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        textPane = new JTextPane();
        textPane.setEditable(false);

        addStyledText(ResourceHelper.getString("welcomeMessage"), null, textPane);

        JScrollPane scrollPane = new JScrollPane(textPane);
        add(scrollPane, BorderLayout.NORTH);

        InputPanel inputPanel = new InputPanel(this);
        add(inputPanel.getPanel(), BorderLayout.CENTER);

        ButtonManager buttonManager = new ButtonManager(this, inputPanel, textPane);
        add(buttonManager.getPanel(), BorderLayout.SOUTH);

        setVisible(true);
    }

    public JTextPane getScanInputViewTextPane(){
        return this.textPane;
    }
}