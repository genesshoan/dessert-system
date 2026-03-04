package ude.edu.uy.taller2.client.controller.sale;

import ude.edu.uy.taller2.client.controller.BaseController;
import ude.edu.uy.taller2.exception.*;
import ude.edu.uy.taller2.ui.sale.SaleCreationDialog;

import java.rmi.RemoteException;
import java.time.LocalDate;

public class CreateSaleController extends BaseController<SaleCreationDialog> {
    public CreateSaleController(SaleCreationDialog view) {
        super(view);
    }

    /**
     * Crea una nueva venta con dirección y fecha indicadas.
     *
     * @param address dirección de la venta
     * @param date    fecha de la venta
     */
    public boolean createSale(String address, LocalDate date) {
        try {
            logicLayer.createSale(address, date);
            return true;
        } catch (RequiredFieldIsEmptyException | InvalidDateException e) {
            showError("Invalid input", e.getMessage());
        } catch (RemoteException e) {
            connectionError();
        }

        return false;
    }
}
