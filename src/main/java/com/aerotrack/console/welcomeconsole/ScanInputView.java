package com.aerotrack.console.welcomeconsole;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import java.awt.BorderLayout;
import java.io.IOException;
import java.util.Objects;

import com.aerotrack.console.welcomeconsole.components.ButtonManager;
import com.aerotrack.console.welcomeconsole.components.InputPanel;
import com.aerotrack.model.entities.AerotrackStage;
import com.aerotrack.utils.ResourceHelper;
import com.aerotrack.utils.clients.api.AerotrackApiClient;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import static com.aerotrack.utils.Utils.addStyledText;

@Slf4j
public class ScanInputView extends JFrame {

    public int baseHeight = 420;
    @Getter
    private final AerotrackApiClient aerotrackApiClient = AerotrackApiClient.create(AerotrackStage.ALPHA);

    public ScanInputView()  {
        try {
            this.setIconImage(ImageIO.read(Objects.requireNonNull(getClass().getResource("/logo.png"))));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        setTitle(ResourceHelper.getString("title"));
        setSize(900, baseHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);

        addStyledText(ResourceHelper.getString("welcomeMessage"), null, textPane);

        JScrollPane scrollPane = new JScrollPane(textPane);
        add(scrollPane, BorderLayout.NORTH);

        InputPanel inputPanel = new InputPanel(this);
        add(inputPanel.getPanel(), BorderLayout.CENTER);

        ButtonManager buttonManager = new ButtonManager(this, inputPanel);
        add(buttonManager.getPanel(), BorderLayout.SOUTH);

        setVisible(true);
    }
}