package ude.edu.uy.taller2.client;

import ude.edu.uy.taller2.client.controller.DessertController;
import ude.edu.uy.taller2.ui.dessert.DessertListPanel;

import javax.swing.*;

public class MainFrame extends JFrame {
    public MainFrame(DessertController dessertController) {
        JTabbedPane tabbedPane = new JTabbedPane();

        setTitle("Desserts system");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        setLocationRelativeTo(null);

        tabbedPane.add("Desserts", new DessertListPanel(dessertController));

        add(tabbedPane);
    }
}
