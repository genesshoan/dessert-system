package ude.edu.uy.taller2.client.controller;

import ude.edu.uy.taller2.Config;
import ude.edu.uy.taller2.logic.ILogicLayer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RMIConnection {
    private static ILogicLayer instance = null;

    public static ILogicLayer getInstance() throws RemoteException, MalformedURLException, NotBoundException {
        if (instance == null) {
            synchronized (RMIConnection.class) {
                instance = (ILogicLayer) Naming.lookup(Config.getURL());
            };
        }
        return instance;
    }
}
