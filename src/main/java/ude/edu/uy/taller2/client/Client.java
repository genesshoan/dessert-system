package ude.edu.uy.taller2.client;

import ude.edu.uy.taller2.Config;
import ude.edu.uy.taller2.client.controller.DessertController;
import ude.edu.uy.taller2.logic.ILogicLayer;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {
    public static void main(String[] args) {
        try {
            ILogicLayer logicLayer = (ILogicLayer) Naming.lookup(Config.getURL());

            DessertController dessertController = new DessertController(logicLayer);

            EventQueue.invokeLater(() -> {
                MainFrame mainFrame = new MainFrame(dessertController);
                mainFrame.setVisible(true);
            });

        } catch (NotBoundException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Server is not available right now",
                    "Connection error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (RemoteException | MalformedURLException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Cannot connect with the server",
                    "Connection error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
