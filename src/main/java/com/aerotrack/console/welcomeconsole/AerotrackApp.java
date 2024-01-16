package com.aerotrack.console.welcomeconsole;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Font;
import java.io.IOException;
import java.util.Objects;

import com.aerotrack.console.welcomeconsole.components.ButtonManager;
import com.aerotrack.console.welcomeconsole.components.InputPanel;
import com.aerotrack.model.entities.AerotrackStage;
import com.aerotrack.utils.clients.api.AerotrackApiClient;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AerotrackApp extends JFrame {

    public int baseHeight = 420;
    @Getter
    private final AerotrackApiClient aerotrackApiClient = AerotrackApiClient.create(AerotrackStage.ALPHA);

    private AerotrackApp()  {
        try {
            this.setIconImage(ImageIO.read(Objects.requireNonNull(getClass().getResource("/logo.png"))));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        setTitle("AeroTrack");
        setSize(900, baseHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        EmptyBorder border = new EmptyBorder(5,20,5,20);

        JLabel titleLabel = new JLabel("Scan Flights");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(border);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        InputPanel inputPanel = new InputPanel(this);
        inputPanel.getPanel().setBorder(border);
        add(inputPanel.getPanel(), BorderLayout.CENTER);

        ButtonManager buttonManager = new ButtonManager(this, inputPanel);
        add(buttonManager.getPanel(), BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AerotrackApp::new);
    }
}