package com.aerotrack.console.welcomeconsole;

import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JWindow {
    public SplashScreen(String imagePath) {
        ImageIcon imageIcon = new ImageIcon(imagePath);
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setSize(200, 200);
        getContentPane().add(imageLabel, BorderLayout.CENTER);
        setBackground(new Color(0, 0, 0, 0));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void close() {
        setVisible(false);
        dispose();
    }
}
