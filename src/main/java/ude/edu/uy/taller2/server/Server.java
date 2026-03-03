package ude.edu.uy.taller2.server;

import ude.edu.uy.taller2.Config;
import ude.edu.uy.taller2.exception.AppDataPersistenceException;
import ude.edu.uy.taller2.logic.LogicLayer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * Clase ejecutable que inicia el servidor RMI de la aplicación.
 * Registra la instancia de {@link ude.edu.uy.taller2.logic.LogicLayer} en
 * el registro RMI usando los valores de configuración provistos por
 * {@link ude.edu.uy.taller2.Config}.
 */
public class Server {
    public static void main(String[] args) {
        try {
            System.out.println("Starting server...");

            LocateRegistry.createRegistry(Config.getPort());

            LogicLayer logicLayer = LogicLayer.getInstance();

            Naming.rebind(Config.getURL(), logicLayer);

            System.out.println("Server is running now...");
        } catch (RemoteException | MalformedURLException e) {
            System.out.println("Something unexpected has occurred");
            e.printStackTrace();
        } catch (AppDataPersistenceException e) {
            System.out.println("Something was wrong while loading data");
            e.printStackTrace();
        }
    }
}
