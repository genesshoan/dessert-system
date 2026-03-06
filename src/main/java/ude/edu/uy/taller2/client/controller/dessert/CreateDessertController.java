package ude.edu.uy.taller2.client.controller.dessert;

import ude.edu.uy.taller2.client.controller.BaseController;
import ude.edu.uy.taller2.dto.DessertDTO;
import ude.edu.uy.taller2.exception.DessertAlreadyExistsException;
import ude.edu.uy.taller2.exception.InvalidAmountException;
import ude.edu.uy.taller2.exception.RequiredFieldIsEmptyException;
import ude.edu.uy.taller2.ui.dessert.DessertCreationForm;

import java.rmi.RemoteException;

public class CreateDessertController extends BaseController<DessertCreationForm> {
    public CreateDessertController(DessertCreationForm view) {
        super(view);
    }

    /**
     * Crea un nuevo postre en el catálogo.
     *
     * @param dessertDTO descripción del postre a crear
     */
    public boolean createDessert(DessertDTO dessertDTO) {
        try {
            logicLayer.createDessert(dessertDTO);
            showInfo("Create dessert operation", "Dessert created successfully");
            return true;
        } catch (RequiredFieldIsEmptyException | DessertAlreadyExistsException | InvalidAmountException e) {
            invalidInput(e);
        } catch (RemoteException e) {
            connectionError();
        }

        return false;
    }
}
