package ude.edu.uy.taller2;

import ude.edu.uy.taller2.client.controller.DessertController;
import ude.edu.uy.taller2.logic.ILogicLayer;
import ude.edu.uy.taller2.ui.dessert.DessertManagementFrame;

import javax.swing.*;
import java.awt.*;
import java.rmi.Naming;

public class TestDessertListPanel {
    public static void main(String[] args) throws Exception {
        ILogicLayer logicLayer = (ILogicLayer) Naming.lookup(Config.getURL());

        DessertController dessertController = new DessertController(logicLayer);

        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame("Test - Dessert List Panel");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setBounds(100, 100, 600, 400);
            frame.setContentPane(new DessertManagementFrame(null));
            frame.setVisible(true);
        });
    }
}