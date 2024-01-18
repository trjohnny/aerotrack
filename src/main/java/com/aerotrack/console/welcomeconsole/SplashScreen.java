package com.aerotrack.console.welcomeconsole;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;
import java.awt.BorderLayout;
import java.awt.Color;
import java.net.URL;

public class SplashScreen extends JWindow {
    public SplashScreen() {
        URL imageUrl = getClass().getResource("/bg_logo.png");
        if (imageUrl == null) {
            throw new IllegalArgumentException("Resource not found");
        }
        JLabel imageLabel = new JLabel( new ImageIcon(imageUrl));
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
