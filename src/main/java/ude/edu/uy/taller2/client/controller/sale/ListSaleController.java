package ude.edu.uy.taller2.client.controller.sale;

import ude.edu.uy.taller2.Config;
import ude.edu.uy.taller2.client.controller.BaseController;
import ude.edu.uy.taller2.dto.SalesSummaryDTO;
import ude.edu.uy.taller2.exception.DessertNotFoundException;
import ude.edu.uy.taller2.exception.RequiredFieldIsEmptyException;
import ude.edu.uy.taller2.server.collection.SaleFilter;
import ude.edu.uy.taller2.dto.SaleDTO;
import ude.edu.uy.taller2.logic.ILogicLayer;
import ude.edu.uy.taller2.ui.sale.SaleManagementFrame;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

public class ListSaleController extends BaseController<SaleManagementFrame> {
    private SaleManagementFrame view;

    public ListSaleController(SaleManagementFrame view) {
        super(view);
    }

    public List<SaleDTO> getSalesByStatus(SaleFilter saleFilter) {
        try {
            return logicLayer.getSalesByStatus(saleFilter);
        } catch (RemoteException e) {
            connectionError();
        }

        return List.of();
    }
}
