package ude.edu.uy.taller2.client.controller.dessert;

import ude.edu.uy.taller2.client.controller.BaseController;
import ude.edu.uy.taller2.dto.DessertDTO;
import ude.edu.uy.taller2.dto.SalesSummaryDTO;
import ude.edu.uy.taller2.exception.DessertNotFoundException;
import ude.edu.uy.taller2.exception.RequiredFieldIsEmptyException;
import ude.edu.uy.taller2.ui.dessert.DessertManagementFrame;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

public class DessertListController extends BaseController<DessertManagementFrame> {
    public DessertListController(DessertManagementFrame view) {
        super(view);
    }

    /**
     * Devuelve todos los postres disponibles.
     *
     * @return lista de {@link DessertDTO}
     */
    public List<DessertDTO> getAllDesserts() {
        try {
            return logicLayer.getAllDesserts();
        } catch (RemoteException e) {
            connectionError();
        }

        return List.of();
    }

    /**
     * Obtiene un postre por su código.
     *
     * @param code código del postre
     * @return {@link DessertDTO} correspondiente
     */
    public DessertDTO getDessertByCode(String code) {
        try {
            return logicLayer.getDessertByCode(code);
        } catch (DessertNotFoundException | RequiredFieldIsEmptyException e) {
            invalidInput(e);
        } catch (RemoteException e) {
            connectionError();
        }

        return null;
    }
}
