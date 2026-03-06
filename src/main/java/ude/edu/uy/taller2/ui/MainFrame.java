package ude.edu.uy.taller2.ui;

import ude.edu.uy.taller2.client.controller.persistence.PersistenceController;
import ude.edu.uy.taller2.ui.dessert.DessertManagementFrame;
import ude.edu.uy.taller2.ui.sale.SaleManagementFrame;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        // Componentes
        JMenuBar menuBar = new JMenuBar();

        JMenu managementMenu = new JMenu("Management");
        JMenu persistenceMenu = new JMenu("Persistence");

        JMenuItem dessertsItem = new JMenuItem("Desserts");
        JMenuItem salesItem = new JMenuItem("Sales");
        JMenuItem saveItem = new JMenuItem("Save");


        ImageIcon raw = new ImageIcon(getClass().getResource("/logo.png"));
        Image scaled = raw.getImage().getScaledInstance(800, 570, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaled));


        // Configuracion
        setTitle("Desserts system");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        menuBar.setPreferredSize(new Dimension(0, 30));

        // Agregar componentes
        managementMenu.add(dessertsItem);
        managementMenu.add(salesItem);
        persistenceMenu.add(saveItem);
        menuBar.add(managementMenu);
        menuBar.add(persistenceMenu);

        setJMenuBar(menuBar);
        add(logoLabel, BorderLayout.CENTER);

        // Actions
        salesItem.addActionListener((e) -> {
            SaleManagementFrame saleManagementFrame = new SaleManagementFrame(this);

            saleManagementFrame.setVisible(saleManagementFrame.isConnected());
        });

        dessertsItem.addActionListener((e) -> {
            DessertManagementFrame dessertManagementFrame = new DessertManagementFrame(this);
            dessertManagementFrame.setVisible(dessertManagementFrame.isConnected());
        });

        saveItem.addActionListener((e) -> {
            PersistenceController persistenceController = new PersistenceController(this);
            if (persistenceController.isConnected()) {
                persistenceController.saveData();
            }
        });
    }
}