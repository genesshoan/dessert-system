package ude.edu.uy.taller2.client.controller;

import ude.edu.uy.taller2.collection.SaleDecision;
import ude.edu.uy.taller2.collection.SaleFilter;
import ude.edu.uy.taller2.dto.DessertDTO;
import ude.edu.uy.taller2.dto.SaleDTO;
import ude.edu.uy.taller2.dto.SaleItemDTO;
import ude.edu.uy.taller2.dto.SalesSummaryDTO;
import ude.edu.uy.taller2.exception.*;
import ude.edu.uy.taller2.logic.ILogicLayer;
import ude.edu.uy.taller2.logic.LogicLayer;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

public class DessertController {
    private final ILogicLayer logicLayer;

    public DessertController(ILogicLayer logicLayer) {
        this.logicLayer = logicLayer;
    }

    /**
     * Crea un nuevo postre en el catálogo.
     *
     * @param dessertDTO descripción del postre a crear
     * @throws RemoteException                   en fallos de comunicación remota
     * @throws RequiredFieldIsEmptyException     si algún campo requerido está vacío
     * @throws DessertAlreadyExistsException     si ya existe un postre con el mismo código
     * @throws InvalidAmountException            si el precio es inválido
     */
    public void createDessert(DessertDTO dessertDTO) throws RemoteException, RequiredFieldIsEmptyException,
            DessertAlreadyExistsException, InvalidAmountException {
        logicLayer.createDessert(dessertDTO);
    }

    /**
     * Devuelve todos los postres disponibles.
     *
     * @return lista de {@link DessertDTO}
     * @throws RemoteException en fallos de comunicación remota
     */
    public List<DessertDTO> getAllDesserts() throws RemoteException {
        return logicLayer.getAllDesserts();
    }

    /**
     * Obtiene un postre por su código.
     *
     * @param code código del postre
     * @return {@link DessertDTO} correspondiente
     * @throws RemoteException               en fallos de comunicación remota
     * @throws RequiredFieldIsEmptyException si el código está vacío
     * @throws DessertNotFoundException      si no existe el postre
     */
    public DessertDTO getDessertByCode(String code) throws RemoteException, RequiredFieldIsEmptyException,
            DessertNotFoundException {
        return logicLayer.getDessertByCode(code);
    }
}
