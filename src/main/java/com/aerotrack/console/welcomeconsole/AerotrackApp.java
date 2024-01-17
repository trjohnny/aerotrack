package com.aerotrack.console.welcomeconsole;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.aerotrack.console.resultconsole.DestinationsButtonsPanel;
import com.aerotrack.console.welcomeconsole.components.ButtonManager;
import com.aerotrack.console.welcomeconsole.components.InputPanel;
import com.aerotrack.model.entities.AerotrackStage;
import com.aerotrack.model.entities.AirportsJsonFile;
import com.aerotrack.model.entities.Trip;
import com.aerotrack.utils.clients.api.AerotrackApiClient;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class AerotrackApp extends JFrame {
    private final AerotrackApiClient aerotrackApiClient = AerotrackApiClient.create(AerotrackStage.ALPHA);
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final DestinationsButtonsPanel destinationPanel ;
    public final AirportsJsonFile airportsJsonFile;


    private AerotrackApp()  {
        try {
            this.setIconImage(ImageIO.read(Objects.requireNonNull(getClass().getResource("/logo.png"))));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        cardLayout = new CardLayout();
        getContentPane().setLayout(cardLayout);

        setTitle("AeroTrack");
        setResizable(false);
        setSize(900, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        airportsJsonFile = aerotrackApiClient.getAirportsJson();

        mainPanel = new JPanel(new BorderLayout());
        destinationPanel = new DestinationsButtonsPanel(this, null, airportsJsonFile);

        EmptyBorder border = new EmptyBorder(5,20,5,20);

        JLabel titleLabel = new JLabel("Scan Flights");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(border);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        InputPanel inputPanel = new InputPanel(this, airportsJsonFile);
        inputPanel.getPanel().setBorder(border);
        mainPanel.add(inputPanel.getPanel(), BorderLayout.CENTER);

        ButtonManager buttonManager = new ButtonManager(this, inputPanel);
        mainPanel.add(buttonManager.getPanel(), BorderLayout.SOUTH);

        getContentPane().add(mainPanel, "MainPanel");
        getContentPane().add(destinationPanel, "DestinationPanel");

        cardLayout.show(getContentPane(), "MainPanel");

        setVisible(true);
    }

    public static void main(String[] args) {
        final SplashScreen splashScreen = new SplashScreen("src/main/resources/bg_logo.png");

        // Create a new thread for the application initialization
        Thread appThread = new Thread(() -> {
            new AerotrackApp(); // Initialize the main application
            SwingUtilities.invokeLater(splashScreen::close); // Close the splash screen
        });

        // Start the application thread
        appThread.start();

        try {
            // Ensure the splash screen is displayed for a minimum of one second
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }

        // If the application is still initializing, wait for it to finish
        try {
            appThread.join();
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

    public void showDestinationPanel(Map<String, List<Trip>> destinationResults) {
        destinationPanel.setDestinationResults(destinationResults);
        cardLayout.show(getContentPane(), "DestinationPanel");
        destinationPanel.initComponents();
    }

    public void showMainPanel() {
        cardLayout.show(getContentPane(), "MainPanel");
    }
}