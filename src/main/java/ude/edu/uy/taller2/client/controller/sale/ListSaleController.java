package ude.edu.uy.taller2.client.controller.sale;

import ude.edu.uy.taller2.Config;
import ude.edu.uy.taller2.collection.SaleFilter;
import ude.edu.uy.taller2.dto.SaleDTO;
import ude.edu.uy.taller2.logic.ILogicLayer;
import ude.edu.uy.taller2.ui.sale.SaleManagementFrame;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

public class ListSaleController {
    private SaleManagementFrame saleManagementFrame;
    private ILogicLayer logicLayer;
    private boolean isConnected = false;

    public ListSaleController(SaleManagementFrame saleManagementFrame) {
        this.saleManagementFrame = saleManagementFrame;

        try {
            logicLayer = (ILogicLayer) Naming.lookup(Config.getURL());
            isConnected = true;
            saleManagementFrame.setVisible(true);
        } catch (RemoteException | MalformedURLException | NotBoundException e) {
            saleManagementFrame.showError("Connection error", "Server is not available right now");
        }
    }

    public boolean isConnected() {
        return isConnected;
    }

    public List<SaleDTO> getSalesByStatus(String option) {
        SaleFilter saleFilter = switch (option) {
            case "Finished" -> SaleFilter.FINISHED;
            case "Pending" -> SaleFilter.PENDING;
            default -> SaleFilter.ALL;
        };

        try {
            return logicLayer.getSalesByStatus(saleFilter);
        } catch (RemoteException e) {
            saleManagementFrame.showError("Connection lost", "Server is not available");
        }

        return List.of();
    }
}
