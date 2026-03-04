package ude.edu.uy.taller2.client.controller;

import ude.edu.uy.taller2.logic.ILogicLayer;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public abstract class BaseController<V extends Component> {
    protected ILogicLayer logicLayer;
    protected V view;
    private boolean isConnected;

    public BaseController(V view) {
        this.view = view;

        try {
            logicLayer = RMIConnection.getInstance();
            isConnected = true;
        } catch (RemoteException | MalformedURLException | NotBoundException e) {
            connectionError();
        }
    }

    public boolean isConnected() {
        return isConnected;
    }

    protected void showError(String title, String message) {
        JOptionPane.showMessageDialog(view, message, title, JOptionPane.ERROR_MESSAGE);
    }

    protected void connectionError() {
        showError("Connection error", "Server is not available right now");
    }

    protected void invalidInput(Exception e) {
        showError("InvalidInput", e.getMessage());
    }
}
