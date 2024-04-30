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
import com.aerotrack.utils.clients.s3.AerotrackS3Client;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class AerotrackApp extends JFrame {
    private static final long MIN_SPLASH_SCREEN_DURATION_MILLIS = 2000;
    private final AerotrackApiClient aerotrackApiClient = AerotrackApiClient.create(AerotrackStage.PROD);
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final DestinationsButtonsPanel destinationPanel ;
    public final AirportsJsonFile airportsJsonFile;


    private AerotrackApp() throws IOException {
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

        airportsJsonFile = aerotrackApiClient.getMergetAirportsJson();

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
    }

    public static void main(String[] args) throws IOException {
        log.error("lkdcnswjkdc");
        final SplashScreen splashScreen = new SplashScreen();

        long startTime = System.currentTimeMillis();

        final AerotrackApp[] app = new AerotrackApp[1];

        app[0] = new AerotrackApp();

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        long sleepDuration = Math.max(MIN_SPLASH_SCREEN_DURATION_MILLIS - duration, 0);
        if (sleepDuration > 0) {
            try {
                Thread.sleep(sleepDuration);
            } catch (InterruptedException e) {
                System.err.println("Splash screen interrupted: " + e.getMessage());
            }
        }

        SwingUtilities.invokeLater(() -> {
            app[0].setVisible(true);
            splashScreen.close();
        });
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