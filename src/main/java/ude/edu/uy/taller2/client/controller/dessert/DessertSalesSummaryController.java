package ude.edu.uy.taller2.client.controller.dessert;

import ude.edu.uy.taller2.client.controller.BaseController;
import ude.edu.uy.taller2.dto.SalesSummaryDTO;
import ude.edu.uy.taller2.exception.DessertNotFoundException;
import ude.edu.uy.taller2.exception.RequiredFieldIsEmptyException;
import ude.edu.uy.taller2.ui.dessert.DessertSummaryDialog;

import javax.swing.*;
import java.rmi.RemoteException;
import java.time.LocalDate;

public class DessertSalesSummaryController extends BaseController<DessertSummaryDialog> {
    public DessertSalesSummaryController(DessertSummaryDialog view) {
        super(view);
    }

    /**
     * Obtiene el resumen de ventas por postre para una fecha dada.
     *
     * @param code código del postre
     * @param date fecha para el resumen
     * @return {@link SalesSummaryDTO} con unidades y monto total
     */
    public void getSalesByDessert(String code, LocalDate date)  {
        view.setSummary(0, "0");
        try {
            SalesSummaryDTO summary = logicLayer.getSalesByDessert(code, date);
            view.setSummary(summary.getTotalUnits(), summary.getTotalAmount().toPlainString());
        } catch (DessertNotFoundException | RequiredFieldIsEmptyException e) {
            invalidInput(e);
        } catch (RemoteException e) {
            connectionError();
        }
    }
}

