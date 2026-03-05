package ude.edu.uy.taller2.client;

import ude.edu.uy.taller2.ui.MainFrame;

import javax.swing.*;
import java.awt.*;

public class Client {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            // si falla usa el look and feel por defecto
        }

        EventQueue.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}
