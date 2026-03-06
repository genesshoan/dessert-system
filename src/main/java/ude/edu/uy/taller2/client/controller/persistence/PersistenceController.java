package ude.edu.uy.taller2.client.controller.persistence;

import ude.edu.uy.taller2.client.controller.BaseController;
import ude.edu.uy.taller2.exception.AppDataPersistenceException;
import ude.edu.uy.taller2.ui.MainFrame;

import java.rmi.RemoteException;

public class PersistenceController extends BaseController<MainFrame> {
    public PersistenceController(MainFrame view) {
        super(view);
    }

    /**
     * Persiste el estado actual de la aplicación.
     */
    public void saveData() {
        try {
            logicLayer.saveData();
            showInfo("Persistence operation", "Data saved successfully");
        } catch (RemoteException e) {
            connectionError();
        } catch (AppDataPersistenceException e) {
            showError("Persistence error", e.getMessage());
        }
    }
}